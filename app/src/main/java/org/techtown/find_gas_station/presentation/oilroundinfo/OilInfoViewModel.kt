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

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    var sortText = ""

    // var conditionChangeFlag  = true
    var oilCondition = OilCondition("1000", "1", "D047")
    // var afterOilCondition = OilCondition("","","")


    private val _oilListLiveData = MutableLiveData<List<TotalOilInfo>>()
    val oilListLiveData: LiveData<List<TotalOilInfo>> get() = _oilListLiveData

    private val stationInfoRepository = StationInfoRepository()
    //TODO("viewModel에서 객체를 생성 -> 추후 Hilt를 적용하여 객체를 주입 받아야 함.")

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

    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }
}