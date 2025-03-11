package org.techtown.find_gas_station.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.techtown.find_gas_station.domain.model.OilCondition
import org.techtown.find_gas_station.domain.usecase.GetLocalOilConditionUseCase
import org.techtown.find_gas_station.domain.usecase.UpdateLocalOilConditionUseCase

class SetViewModel() : ViewModel() {

    private val getLocalOilConditionUseCase : GetLocalOilConditionUseCase
        = GetLocalOilConditionUseCase()
    private val updateLocalOilCondition : UpdateLocalOilConditionUseCase
        = UpdateLocalOilConditionUseCase()

    private val _roomDbOilCondition = MutableStateFlow(OilCondition("B027", "1000", "1"))
    val roomDbOilCondition = _roomDbOilCondition.filterNotNull().stateIn(
        initialValue = OilCondition("B027", "1000", "1"),
        started = SharingStarted.WhileSubscribed(5_000),
        scope = viewModelScope
    )

    private val _updateComplete = MutableStateFlow(false)
    val updateComplete = _updateComplete.stateIn(
        initialValue = false,
        started = SharingStarted.WhileSubscribed(5_000),
        scope = viewModelScope
    )

    init {
        requestLocalOilCondition()
    }

    private fun requestLocalOilCondition() {
        viewModelScope.launch(Dispatchers.IO) {
            _roomDbOilCondition.value = getLocalOilConditionUseCase.invoke()
        }
    }

    fun updateLocalOilCondition(oilCondition: OilCondition) {
        viewModelScope.launch(Dispatchers.IO) {
            updateLocalOilCondition.invoke(oilCondition)
            _updateComplete.value = true
        }
    }
}
