package org.techtown.find_gas_station.presentation.ui.oilavginfo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.techtown.find_gas_station.domain.model.OilAveragePriceInfo
import org.techtown.find_gas_station.domain.usecase.GetOilAvgPriceUseCase

class OilAvgViewModel() : ViewModel() {

    private val getOilAvgPriceUseCase : GetOilAvgPriceUseCase = GetOilAvgPriceUseCase()

    private val _oilAvgList = MutableStateFlow<List<OilAveragePriceInfo>>(emptyList())
    val oilAvgList: StateFlow<List<OilAveragePriceInfo>> get() = _oilAvgList

    fun fetchOilAvg(prodCd: String) {
        viewModelScope.launch {
            getOilAvgPriceUseCase.invoke(prodCd)
                .catch { e -> Log.e("TAG", e.message.toString()) }
                .collect { _oilAvgList.value = it }
        }
    }
}