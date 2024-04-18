package org.techtown.find_gas_station.localdatabase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.techtown.find_gas_station.repository.SetRepository

class SetViewModel(application: Application) : AndroidViewModel(application) {


    var localOilCondition : OilData?
    private val setRepository: SetRepository

    init {
        localOilCondition = OilData("D047", "1000", "1")
        val oilDao = RoomDB.getAppDatabase(application).setDao()
        setRepository = SetRepository(oilDao)
        requestLocalOilCondition()
    }

    private fun requestLocalOilCondition() {
        viewModelScope.launch(Dispatchers.IO) {
            val localData = setRepository.getOilLocalData()

            withContext(Dispatchers.Main) {
                localOilCondition = localData
            }
        }
    }

    fun updateData(set: OilData) {

        viewModelScope.launch(Dispatchers.IO) {
            setRepository.deleteAll()
            setRepository.insert(set)
        }
    }

}
