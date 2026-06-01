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
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import fi.nutrifier.ui.components.dialogs.BarCodeAnalyzer
import fi.nutrifier.viewmodels.BarScanState
import fi.nutrifier.viewmodels.ViewModelWrapper
import com.google.common.util.concurrent.ListenableFuture
import fi.nutrifier.ui.components.buttons.BackButton
import fi.nutrifier.ui.components.inputs.ActionButtons
import fi.nutrifier.ui.components.layout.TopBar
import fi.nutrifier.ui.components.misc.TitleSubtitle
import fi.nutrifier.utils.Enums
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Composable
fun BarcodeScreen(
    navController: NavController,
    viewModels: ViewModelWrapper,
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

    fun handleBackNavigation() {
        when (useCase) {
            "ADD_FOODS" -> navController.navigate("add_food/${viewModels.barcode.barScanResult}") {
                popUpTo("barcode") { inclusive = true }
            }
            "EDIT_FOOD" -> navController.navigate("food_editor/${Enums.FoodMode.CREATE_ENTRY}/${viewModels.barcode.barScanResult ?: ""}") {
                popUpTo("barcode") { inclusive = true }
            }
            else -> navController.popBackStack("barcode", true)
        }
    }

    LaunchedEffect(barScanState) {
        if (barScanState is BarScanState.ScanSuccess) {
            val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
                ProcessCameraProvider.getInstance(context)

            val cameraProvider = cameraProviderFuture.get()

            cameraProvider.unbindAll()

            handleBackNavigation()

            viewModels.barcode.resetState()
        }
    }

    BaseScreen(
        topBar = {
            TopBar(subtitle = { BackButton(
                navController = navController,
                onNavigation = { handleBackNavigation() }
            ) })
        },
        bottomBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            ) {
                ActionButtons(onSecondaryAction = { handleBackNavigation() })
            }
        },
        screen = Enums.Screen.BARCODE,
        viewModels,
        navController,
    ) {
        Column(modifier = Modifier.padding(horizontal = 8.dp)) {
            TitleSubtitle(
                title = "Barcode Scanner",
                subtitle = "Point your camera towards the barcode you want to scan.",
                alignment = Alignment.Start,
                subtitleStyle = MaterialTheme.typography.labelMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.weight(1f)) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp)
                    ),
                    factory = { androidViewContext ->
                        PreviewView(androidViewContext).apply {
                            this.scaleType = PreviewView.ScaleType.FILL_START
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT,
                            )
                            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                        }
                    }
                ) { previewView ->
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
                }
                val overlayColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                Canvas(
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(16.dp))
                ) {
                    val holeHeight = 150.dp.toPx()
                    val holeWidth = 300.dp.toPx()
                    val left = (size.width - holeWidth) / 2
                    val top = (size.height - holeHeight) / 2
                    val cornerRadius = 24.dp.toPx()

                    val cutout = RoundRect(
                        left = left,
                        top = top,
                        right = left + holeWidth,
                        bottom = top + holeHeight,
                        cornerRadius = CornerRadius(cornerRadius)
                    )

                    val overlayPath = Path().apply {
                        addRect(Rect(Offset.Zero, size))
                        addRoundRect(cutout)
                        fillType = PathFillType.EvenOdd
                    }

                    drawPath(
                        path = overlayPath,
                        color = overlayColor
                    )
                }
            }
        }
    }

    /*
    Scaffold(
        floatingActionButton = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                ActionButtons(
                    onSecondaryAction = { navController.navigateUp() },
                    onPrimaryAction = {
                        Log.d("BARCODE", "TODO: Implement barcode after reading")
                    },
                )
            }
        }
    ) {
        AndroidView(
            modifier = Modifier.padding(it).fillMaxSize(),
            factory = { androidViewContext ->
                PreviewView(androidViewContext).apply {
                    this.scaleType = PreviewView.ScaleType.FILL_START
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                }
            },
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
     */
}