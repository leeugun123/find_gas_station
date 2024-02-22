package org.techtown.find_gas_station.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.techtown.find_gas_station.Data.oilAvg.OilAveragePriceInfo
import org.techtown.find_gas_station.Repository.GetOilAvgRepository

class GetOilAvgViewModel() : ViewModel() {

    private var _oilAvgInfoLiveData : MutableLiveData<List<OilAveragePriceInfo>> = MutableLiveData()
    val oilAvgLiveData : LiveData<List<OilAveragePriceInfo>> get() = _oilAvgInfoLiveData

    private var getOilAvgRepository = GetOilAvgRepository()

    fun requestOilAvg(prodcd : String) {

        viewModelScope.launch(Dispatchers.IO) {

            val getOilAvgResponse = getOilAvgRepository.getOilAvg(prodcd)

            withContext(Dispatchers.Main){
                _oilAvgInfoLiveData.value = getOilAvgResponse
            }

        }

    }


}