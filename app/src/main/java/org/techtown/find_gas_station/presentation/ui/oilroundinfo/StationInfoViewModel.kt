package org.techtown.find_gas_station.presentation.ui.oilroundinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.techtown.find_gas_station.data.TotalOilInfo
import org.techtown.find_gas_station.data.repository.module.RepositoryModule

class StationInfoViewModel() : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading

    var sortText = ""

    var conditionChangeFlag = true
    var oilCondition = OilCondition("1000", "1", "D047")
    var afterOilCondition = OilCondition("", "", "")

    private val _oilListFlow = MutableStateFlow<List<TotalOilInfo>>(emptyList())
    val oilListFlow: StateFlow<List<TotalOilInfo>>
        get() = _oilListFlow

    private val stationInfoRepository = RepositoryModule.provideStationInfoRepository()

    fun requestOilList(wgsX: String, wgsY: String, katecX: String, katecY: String) {

        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true

            stationInfoRepository.requestStationList(
                wgsX,
                wgsY,
                katecX,
                katecY,
                oilCondition.radius,
                oilCondition.sort,
                oilCondition.oilKind
            )

            val collectStationList = stationInfoRepository.getStationList()
            _oilListFlow.emit(collectStationList)

            _isLoading.value = false
        }
    }
}
