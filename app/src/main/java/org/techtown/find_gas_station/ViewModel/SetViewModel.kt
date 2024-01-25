package org.techtown.find_gas_station.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.techtown.find_gas_station.Data.set.OilData
import org.techtown.find_gas_station.Repository.SetRepository
import org.techtown.find_gas_station.Data.set.RoomDB

class SetViewModel(application: Application) : AndroidViewModel(application) {

    private val _oilLocalData = MutableLiveData<OilData>()
    val oilLocalData : LiveData<OilData> get() = _oilLocalData
    private val setRepository: SetRepository

    init {
        val oilDao = RoomDB.getAppDatabase(application).setDao()
        setRepository = SetRepository(oilDao)
    }

    fun getOilLocalData(){
        viewModelScope.launch {
            _oilLocalData.value = setRepository.getOilLocalData()
        }
    }

    // viewModelScope로 변경
    suspend fun insert(set: OilData) {
        setRepository.insert(set)
    }

    // viewModelScope로 변경
    suspend fun delete()  {
        setRepository.deleteAll()
    }

    class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>) = SetViewModel(application) as T
    }
}
