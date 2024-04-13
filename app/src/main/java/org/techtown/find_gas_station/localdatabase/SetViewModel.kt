package org.techtown.find_gas_station.localdatabase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.techtown.find_gas_station.repository.SetRepository

class SetViewModel(application: Application) : AndroidViewModel(application) {


    lateinit var localOilCondition : OilData
    private val setRepository: SetRepository

    init {
        val oilDao = RoomDB.getAppDatabase(application).setDao()
        setRepository = SetRepository(oilDao)
        requestLocalOilCondtion()
    }

    private fun requestLocalOilCondtion(){
        viewModelScope.launch(Dispatchers.IO){
            val localData = setRepository.getOilLocalData()

            withContext(Dispatchers.Main){
                localOilCondition = localData
            }
        }
    }
    fun updateData(set: OilData) {

        viewModelScope.launch(Dispatchers.IO){
            setRepository.deleteAll()
            setRepository.insert(set)
        }
    }

}
