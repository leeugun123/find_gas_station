package org.techtown.find_gas_station.presentation.ui.oilroundinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.techtown.find_gas_station.data.remote.model.station.TotalOilInfo
import org.techtown.find_gas_station.presentation.di.RepositoryModule

class StationInfoViewModel : ViewModel() {

    val isLoading = MutableStateFlow(false)
    val isEmpty = MutableStateFlow(false)

    private val _stationList = MutableStateFlow<List<TotalOilInfo>>(emptyList())
    val stationList: StateFlow<List<TotalOilInfo>> get() = _stationList

    var sortText = ""

    var isConditionChange = true
    var oilCondition = OilCondition("1000", "1", "D047")
    var afterOilCondition = OilCondition("", "", "")

    private val stationInfoRepository = RepositoryModule.provideStationInfoRepository()

    fun requestOilList(
        wgsX: String, wgsY: String, katecX: String, katecY: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            syncLoading(LoadingState.LOADING)
            stationInfoRepository.requestStationList(
                wgsX,
                wgsY,
                katecX,
                katecY,
                oilCondition.radius,
                oilCondition.sort,
                oilCondition.oilKind
            ).catch { e -> }
            .collect { listData ->
                _stationList.value = listData
                syncEmptyCheck(listData.size)
            }
            syncLoading(LoadingState.NOT_LOADING)
        }
    }

    private fun syncLoading(loadingState: LoadingState) {
        when (loadingState) {
            LoadingState.LOADING -> isLoading.value = true
            LoadingState.NOT_LOADING -> isLoading.value = false
        }
    }

    private fun syncEmptyCheck(size: Int) {
        isEmpty.value = size == 0
    }
}
