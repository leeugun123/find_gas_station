package org.techtown.find_gas_station.presentation.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.techtown.find_gas_station.data.local.model.OilData
import org.techtown.find_gas_station.data.local.RoomDB
import org.techtown.find_gas_station.data.repository.LocalRepository

class SetViewModel(application: Application) : AndroidViewModel(application) {

    private val _roomDbOilCondition = MutableStateFlow(OilData("B027", "1000", "1"))
    val roomDbOilCondition = _roomDbOilCondition.filterNotNull().stateIn(
        initialValue = OilData("B027", "1000", "1"),
        started = SharingStarted.WhileSubscribed(5_000),
        scope = viewModelScope
    )

    private val _updateComplete = MutableStateFlow(false)
    val updateComplete = _updateComplete.stateIn(
        initialValue = false,
        started = SharingStarted.WhileSubscribed(5_000),
        scope = viewModelScope
    )

    private val setRepository: LocalRepository
    // TODO("HilT로 변형)

    init {
        val oilDao = RoomDB.getAppDatabase(application).setDao()
        setRepository = LocalRepository(oilDao)
        requestLocalOilCondition()
    }

    private fun requestLocalOilCondition() {
        viewModelScope.launch(Dispatchers.IO) {
            _roomDbOilCondition.value = setRepository.getOilLocalData()
        }
    }

    fun updateData(set: OilData) {
        viewModelScope.launch(Dispatchers.IO) {
            setRepository.deleteAll()
            setRepository.insert(set)
            _updateComplete.value = true
        }
    }
}
