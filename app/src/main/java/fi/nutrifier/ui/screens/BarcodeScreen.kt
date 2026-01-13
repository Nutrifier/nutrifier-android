package fi.nutrifier.ui.screens

import android.content.pm.PackageManager
import android.util.Log
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import fi.nutrifier.ui.components.buttons.BackButton
import fi.nutrifier.ui.components.dialogs.BarCodeAnalyzer
import fi.nutrifier.ui.components.inputs.CancelSaveOption
import fi.nutrifier.ui.components.layout.TopBar
import fi.nutrifier.utils.Constants
import fi.nutrifier.viewmodels.BarScanState
import fi.nutrifier.viewmodels.ViewModelWrapper
import com.google.common.util.concurrent.ListenableFuture
import fi.nutrifier.ui.screens.settings.sections.ProfileSection
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Composable
fun BarcodeScreen(
    navController: NavController,
    viewModels: ViewModelWrapper,
    snackbarHostState: SnackbarHostState,
    useCase: String?
) {
    val context = LocalContext.current
    var preview by remember { mutableStateOf<Preview?>(null) }
    var permissionGranted by remember { mutableStateOf(false) }
    val barScanState = viewModels.barcode.barScanState
    val lifecycleOwner = LocalLifecycleOwner.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            permissionGranted = isGranted
        }
    )

    LaunchedEffect(Unit) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.CAMERA
            ) -> {
                permissionGranted = true
            }
            else -> {
                permissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
        }
    }

    LaunchedEffect(barScanState) {
        if (barScanState is BarScanState.ScanSuccess) {
            val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
                ProcessCameraProvider.getInstance(context)

            val cameraProvider = cameraProviderFuture.get()

            cameraProvider.unbindAll()

            when (useCase) {
                "ADD_FOODS" -> navController.navigate("add_food/${viewModels.barcode.barScanResult}") {
                    popUpTo("barcode") { inclusive = true }
                }
                else -> navController.popBackStack("barcode", true)
            }

            viewModels.barcode.resetState()
        }
    }

    Screen(
        topBar = { TopBar(subtitle = { BackButton(navController) }) },
        bottomBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                CancelSaveOption(onClose = { navController.navigateUp() }) {
                    Log.d("BARCODE", "Implement barcode after reading")
                }
            }
        },
        screen = Constants.Screen.BARCODE,
        viewModels,
        navController,
        snackbarHostState,
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
        ) {
            Text(text = "Scan barcode", style = MaterialTheme.typography.headlineMedium)
            Text(text = "State: ${viewModels.barcode.barScanState}")
            AndroidView(factory = { androidViewContext ->
                PreviewView(androidViewContext).apply {
                    this.scaleType = PreviewView.ScaleType.FILL_START
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                }
            }, modifier = Modifier.fillMaxSize(),
            update = { previewView ->
                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()
                val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
                val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
                    ProcessCameraProvider.getInstance(context)

                cameraProviderFuture.addListener({
                    preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                    val barcodeAnalyzer = BarCodeAnalyzer(viewModels.barcode)
                    val imageAnalysis: ImageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                        .also {
                            it.setAnalyzer(cameraExecutor, barcodeAnalyzer)
                        }
                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageAnalysis
                        )
                    } catch (e: Exception) {
                        Log.d("TAG", "CameraPreview: ${e.localizedMessage}")
                    }
                }, ContextCompat.getMainExecutor(context))
            })
        }

        when (barScanState) {
            is BarScanState.Ideal -> {
                Column {
                    Text(text = "Position the barcode in front of the camera")
                }
            }

            is BarScanState.Loading -> {
                Column {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Scanning...")
                }
            }

            is BarScanState.ScanSuccess -> {
                Column {
                    Text(text = "Barcode data: $barScanState")
                    Row {
                        Button(onClick = { viewModels.barcode.resetState() }) {
                            Text(text = "Done")
                        }
                    }
                }
            }

            is BarScanState.Error -> {
                Column {
                    Text(text = "Error: ${barScanState.error}")
                }
            }

            else -> {
                Text(text = "What?!")
            }
        }
    }
}