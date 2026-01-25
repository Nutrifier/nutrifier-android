package fi.nutrifier.viewmodels

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import fi.nutrifier.BuildConfig
import fi.nutrifier.models.other.BarModel
import com.google.mlkit.vision.barcode.common.Barcode
import fi.nutrifier.repositories.database.AuthRepository
import kotlinx.coroutines.launch

interface BarScanState {
    data object Ideal : BarScanState
    data class ScanSuccess(val barStateModel: BarModel) : BarScanState
    data class Error(val error: String) : BarScanState
    data object Loading : BarScanState
}

class BarcodeScannerViewModel(encryptedSharedPreferences: SharedPreferences): BaseViewModel(encryptedSharedPreferences) {
    private var _barScanState: MutableState<BarScanState?> = mutableStateOf(null)
    private var _barScanResult: MutableState<String?> = mutableStateOf(null)

    val barScanState get() = _barScanState.value
    val barScanResult get() = _barScanResult.value

    fun onBarCodeDetected(barcodes: List<Barcode>) {
        viewModelScope.launch {
            if (barcodes.isEmpty()) {
                _barScanState.value = BarScanState.Error("No barcode detected")
                return@launch
            }

            barcodes.forEach { barcode ->
                barcode.rawValue?.let { barcodeValue ->
                    try {
                        Log.d("BARCODE", "Barcode value: $barcodeValue")
                        _barScanState.value = BarScanState.ScanSuccess(barStateModel = BarModel(barcodeValue))
                        _barScanResult.value = barcodeValue
                    } catch (e: Exception) {
                        Log.i("BARCODE", "onBarCodeDetected: $e", )
                        _barScanState.value = BarScanState.Error("Invalid JSON format in barcode")
                    }
                    return@launch
                }
            }
            _barScanState.value = BarScanState.Error("No valid barcode value")
        }
    }

    fun resetState() {
        _barScanState.value = BarScanState.Ideal
    }
}