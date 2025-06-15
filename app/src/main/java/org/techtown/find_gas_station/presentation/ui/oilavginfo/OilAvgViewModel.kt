package org.techtown.find_gas_station.presentation.ui.oilavginfo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.techtown.find_gas_station.domain.model.OilAveragePriceInfo
import org.techtown.find_gas_station.domain.usecase.GetOilAvgPriceUseCase
import org.techtown.find_gas_station.domain.usecase.RequestStationListUseCase
import javax.inject.Inject

@HiltViewModel
class OilAvgViewModel @Inject constructor(
    private val getOilAvgPriceUseCase: GetOilAvgPriceUseCase
) : ViewModel() {

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