package org.techtown.find_gas_station.presentation.oilroundinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.techtown.find_gas_station.Data.TotalOilInfo


class OilInfoViewModel : ViewModel() {

    private val stationInfoRepository: StationInfoRepository = TODO()

    var oilCondition = OilCondition("1000", "1", "B027")

    var processing: Boolean = false
    var sortText = ""


    private val _oilListLiveData = MutableLiveData<List<TotalOilInfo>>()
    val oilListLiveData: LiveData<List<TotalOilInfo>> get() = _oilListLiveData

    fun requestOilList(wgsX: String, wgsY: String, katecX: String, katecY: String) {

        viewModelScope.launch(Dispatchers.IO) {
            stationInfoRepository.requestOilList(
                wgsX,
                wgsY,
                katecX,
                katecY,
                oilCondition.radius,
                oilCondition.sort,
                oilCondition.oilKind
            )
            withContext(Dispatchers.Main) {
                _oilListLiveData.value = stationInfoRepository.getOilList()
            }
        }
    }
}