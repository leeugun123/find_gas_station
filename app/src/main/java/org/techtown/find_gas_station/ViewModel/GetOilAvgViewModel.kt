package org.techtown.find_gas_station.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import org.techtown.find_gas_station.Data.oilAvg.OilAveragePriceInfo
import org.techtown.find_gas_station.Repository.GetOilAvgRepository

class GetOilAvgViewModel(application : Application) : AndroidViewModel(application) {

    private var getOilAvgRepository : GetOilAvgRepository
    private var oilAvgLiveData : LiveData<List<OilAveragePriceInfo>>

    init {
        getOilAvgRepository = GetOilAvgRepository(application)
        oilAvgLiveData = getOilAvgRepository.getOilAvgInfoLiveData()
    }

    fun requestOilAvg(prodcd : String) {
        getOilAvgRepository.getOilAvg(prodcd)
    }

    fun getOilAvg() = oilAvgLiveData


}