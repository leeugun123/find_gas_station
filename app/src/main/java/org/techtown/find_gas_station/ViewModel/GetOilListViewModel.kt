package org.techtown.find_gas_station.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.techtown.find_gas_station.Data.TotalOilInfo
import org.techtown.find_gas_station.Repository.GetOilRepository

class GetOilListViewModel(application: Application) : AndroidViewModel(application) {

    private val _oilListLiveData = MutableLiveData<List<TotalOilInfo>>()
    val oilListLiveData : LiveData<List<TotalOilInfo>> get() = _oilListLiveData

    private var getOilRepository : GetOilRepository

    init {
        getOilRepository = GetOilRepository(application)
    }

    suspend fun requestOilList(xPos: String , yPos : String , radius : String , sort : String, oilKind : String) {
        getOilRepository.requestOilList(xPos, yPos, radius, sort, oilKind)
        _oilListLiveData.value = getOilRepository.getOilList()
    }



}