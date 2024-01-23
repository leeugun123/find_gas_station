package org.techtown.find_gas_station.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import org.techtown.find_gas_station.Data.TotalOilInfo
import org.techtown.find_gas_station.Repository.GetOilRepository

class GetOilListViewModel(application: Application) : AndroidViewModel(application) {

    private var getOilRepository : GetOilRepository

    init {
        Log.e("TAG" , "GetOilListViewModel _ init")
        getOilRepository = GetOilRepository(application)
    }

    suspend fun requestOilList(xPos: String , yPos : String , radius : String , sort : String, oilKind : String) {
        Log.e("TAG" , "GetOilListViewModel _ requestOilList")
        getOilRepository.searchOilList(xPos, yPos, radius, sort, oilKind)
    }
    fun getOilList() : LiveData<List<TotalOilInfo>> {
        Log.e("TAG" , "GetOilListViewModel _ requestOilList")
        return getOilRepository.oilListLiveData
    }

}