package org.techtown.find_gas_station.presentation.ui.oilroundinfo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.techtown.find_gas_station.domain.model.StationDetailInfo
import org.techtown.find_gas_station.domain.model.OilCondition
import org.techtown.find_gas_station.domain.usecase.RequestStationListUseCase

class StationInfoViewModel : ViewModel() {

    val loadingState = MutableStateFlow(LoadingState.INIT)
    val isEmpty = MutableStateFlow(false)

    var sortText = ""
    var isConditionChange = false

    var oilCondition = OilCondition("1000", "1", "D047")
    var afterOilCondition = OilCondition("", "", "")

    var localGasStationList: List<StationDetailInfo> = emptyList()

    private val _stationList = MutableStateFlow<List<StationDetailInfo>>(emptyList())
    val stationList: StateFlow<List<StationDetailInfo>> get() = _stationList

    private val requestStationUseCase : RequestStationListUseCase = RequestStationListUseCase()

    fun requestStationList(
        wgsX: String, wgsY: String, katecX: String, katecY: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.e("TAG","dd")
            loadingState.value = LoadingState.LOADING

            requestStationUseCase.invoke(
                wgsX,
                wgsY,
                katecX,
                katecY,
                oilCondition
            ).collect { listData ->
                _stationList.value = listData
                isEmpty.value = listData.isEmpty()
                loadingState.value = LoadingState.COMPLETE
            }
        }
    }
}
