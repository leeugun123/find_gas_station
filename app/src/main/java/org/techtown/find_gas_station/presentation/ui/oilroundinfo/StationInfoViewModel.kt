package org.techtown.find_gas_station.presentation.ui.oilroundinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.techtown.find_gas_station.domain.model.TotalOilInfo
import org.techtown.find_gas_station.domain.model.OilCondition
import org.techtown.find_gas_station.domain.usecase.RequestStationListUseCase

class StationInfoViewModel : ViewModel() {

    val isLoading = MutableStateFlow(false)
    val isEmpty = MutableStateFlow(false)

    private val _stationList = MutableStateFlow<List<TotalOilInfo>>(emptyList())
    val stationList: StateFlow<List<TotalOilInfo>> get() = _stationList

    var sortText = ""
    var isConditionChange = false

    var oilCondition = OilCondition("1000", "1", "D047")
    var afterOilCondition = OilCondition("", "", "")

    var localGasStationList: List<TotalOilInfo> = emptyList()

    private val requestStationUseCase : RequestStationListUseCase = RequestStationListUseCase()

    fun requestStationList(
        wgsX: String, wgsY: String, katecX: String, katecY: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            syncLoading(LoadingState.LOADING)

            requestStationUseCase.invoke(
                wgsX,
                wgsY,
                katecX,
                katecY,
                oilCondition
            ).collect { listData ->
                _stationList.value = listData

                syncIsEmpty(listData.size)
                syncLoading(LoadingState.COMPLETE)
            }
        }
    }

    private fun syncLoading(loadingState: LoadingState) {
        when (loadingState) {
            LoadingState.LOADING -> isLoading.value = true
            LoadingState.COMPLETE -> isLoading.value = false
        }
    }

    private fun syncIsEmpty(size: Int) {
        isEmpty.value = size == 0
    }
}
