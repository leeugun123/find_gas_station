package org.techtown.find_gas_station.presentation.ui.oilroundinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.techtown.find_gas_station.data.TotalOilInfo
import org.techtown.find_gas_station.data.repository.module.RepositoryModule

class StationInfoViewModel() : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    var sortText = ""

    var conditionChangeFlag = true
    var oilCondition = OilCondition("1000", "1", "D047")
    var afterOilCondition = OilCondition("", "", "")

    private val _oilListLiveData = MutableLiveData<List<TotalOilInfo>>()
    val oilListLiveData: LiveData<List<TotalOilInfo>>
        get() = _oilListLiveData

    private val stationInfoRepository = RepositoryModule.provideStationInfoRepository()

    fun requestOilList(wgsX: String, wgsY: String, katecX: String, katecY: String) {

        viewModelScope.launch(Dispatchers.IO) {
            stationInfoRepository.requestStationList(
                wgsX,
                wgsY,
                katecX,
                katecY,
                oilCondition.radius,
                oilCondition.sort,
                oilCondition.oilKind
            )

            withContext(Dispatchers.Main) {
                _oilListLiveData.value = stationInfoRepository.getStationList()
            }
        }
    }

    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }
}