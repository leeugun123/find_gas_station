package org.techtown.find_gas_station.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.techtown.find_gas_station.Data.TotalOilInfo
import org.techtown.find_gas_station.Repository.GetOilRepository

class GetOilListViewModel() : ViewModel() {

    private val _oilListLiveData = MutableLiveData<List<TotalOilInfo>>()
    val oilListLiveData : LiveData<List<TotalOilInfo>> get() = _oilListLiveData

    private var getOilRepository = GetOilRepository()

    fun requestOilList(xPos: String , yPos : String , radius : String , sort : String, oilKind : String) {

        viewModelScope.launch(Dispatchers.IO) {

            getOilRepository.requestOilList(xPos, yPos, radius, sort, oilKind)

            withContext(Dispatchers.Main){
                _oilListLiveData.value = getOilRepository.getOilList()
            }

        }

    }



}