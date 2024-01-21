package org.techtown.find_gas_station.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.coroutineScope
import org.techtown.find_gas_station.Data.set.OilData
import org.techtown.find_gas_station.Repository.SetRepository
import org.techtown.find_gas_station.Data.set.RoomDB

class SetViewModel(application : Application) : AndroidViewModel(application) {

    val oilLocalData : LiveData<OilData>
    private val setRepository : SetRepository

    init {
        val oilDao = RoomDB.getAppDatabase(application).setDao()
        setRepository = SetRepository(oilDao)
        oilLocalData = setRepository.allSets
    }

    suspend fun insert(set: OilData) = coroutineScope {
        setRepository.insert(set)
    }

    suspend fun delete() = coroutineScope {
        setRepository.deleteAll()
    }

    class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>) = SetViewModel(application) as T
    }


}