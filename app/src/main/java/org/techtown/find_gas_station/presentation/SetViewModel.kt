package org.techtown.find_gas_station.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.techtown.find_gas_station.data.localdatabase.OilData
import org.techtown.find_gas_station.data.localdatabase.RoomDB
import org.techtown.find_gas_station.data.repository.SetRepository

class SetViewModel(application: Application) : AndroidViewModel(application) {

    private val _roomDbOilCondition = MutableLiveData<OilData>()
    val roomDbOilCondition: LiveData<OilData> get() = _roomDbOilCondition

    private val _updateComplete = MutableLiveData<Boolean>()
    val updateComplete: LiveData<Boolean> get() = _updateComplete

    private val setRepository: SetRepository
    // TODO("HilT로 변형)

    init {
        val oilDao = RoomDB.getAppDatabase(application).setDao()
        setRepository = SetRepository(oilDao)
        requestLocalOilCondition()
    }

    private fun requestLocalOilCondition() {
        viewModelScope.launch(Dispatchers.IO) {
            val localData = setRepository.getOilLocalData()
            withContext(Dispatchers.Main) {
                _roomDbOilCondition.value = localData
            }
        }
    }

    fun updateData(set: OilData) {
        viewModelScope.launch(Dispatchers.IO) {
            setRepository.deleteAll()
            setRepository.insert(set)
            withContext(Dispatchers.Main) {
                _updateComplete.value = true
            }
        }
    }
}
