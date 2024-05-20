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

class StationInfoViewModel() : ViewModel() {

    val isLoading = MutableStateFlow(false)

    val emptyCheck = MutableStateFlow(false)

    private val _stationList = MutableStateFlow<List<TotalOilInfo>>(emptyList())
    val stationList: StateFlow<List<TotalOilInfo>> get() = _stationList

    var sortText = ""

    var conditionChangeFlag = true
    var oilCondition = OilCondition("1000", "1", "D047")
    var afterOilCondition = OilCondition("", "", "")

    private val stationInfoRepository = RepositoryModule.provideStationInfoRepository()

    fun requestOilList(
        wgsX: String, wgsY: String, katecX: String, katecY: String
    ) {

        viewModelScope.launch(Dispatchers.IO) {

            setLoading(LoadingState.LOADING)

            stationInfoRepository.requestStationList(
                wgsX,
                wgsY,
                katecX,
                katecY,
                oilCondition.radius,
                oilCondition.sort,
                oilCondition.oilKind
            ).catch { e -> }
            .collect {
                _stationList.value = it
                checkList(it.size)
            }

            setLoading(LoadingState.NOT_LOADING)
        }
    }

    private fun setLoading(loadingState: LoadingState) {
        when (loadingState) {
            LoadingState.LOADING -> isLoading.value = true
            LoadingState.NOT_LOADING -> isLoading.value = false
        }
    }

    private fun checkList(size: Int) {
        if (size == 0)
            setEmptyCheck(true)
        else
            setEmptyCheck(false)
    }

    private fun setEmptyCheck(check: Boolean) {
        emptyCheck.value = check
    }
}
