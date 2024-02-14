package org.techtown.find_gas_station.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.techtown.find_gas_station.Data.oilAvg.OilAveragePriceInfo
import org.techtown.find_gas_station.Repository.GetOilAvgRepository

class GetOilAvgViewModel(application : Application) : AndroidViewModel(application) {


    private var _oilAvgInfoLiveData : MutableLiveData<List<OilAveragePriceInfo>> = MutableLiveData()
    val oilAvgLiveData : LiveData<List<OilAveragePriceInfo>>  get() = _oilAvgInfoLiveData

    private var getOilAvgRepository : GetOilAvgRepository


    init {
        getOilAvgRepository = GetOilAvgRepository(application)
    }

    suspend fun requestOilAvg(prodcd : String) {
        getOilAvgRepository.requestOilAvg(prodcd)
        _oilAvgInfoLiveData.value = getOilAvgRepository.getOilAvgList()
    }


}