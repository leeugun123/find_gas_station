package org.techtown.find_gas_station.presentation.ui.oilroundinfo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    private val _oilList = MutableLiveData<List<TotalOilInfo>>()
    val oilList: MutableLiveData<List<TotalOilInfo>>
        get() = _oilList

    private val stationInfoRepository = RepositoryModule.provideStationInfoRepository()

    fun requestOilList(wgsX: String, wgsY: String, katecX: String, katecY: String) {

        viewModelScope.launch(Dispatchers.IO) {
            setLoading(true)
            stationInfoRepository.requestStationList(
                wgsX,
                wgsY,
                katecX,
                katecY,
                oilCondition.radius,
                oilCondition.sort,
                oilCondition.oilKind
            )

            withContext(Dispatchers.Main){
                _oilList.value = stationInfoRepository.getStationList()
                setLoading(false)
            }
        }
    }

    private fun setLoading(loading: Boolean) {
        _isLoading.value = loading
    }
}
