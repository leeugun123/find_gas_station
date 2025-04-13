package org.techtown.find_gas_station.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.techtown.find_gas_station.domain.model.OilCondition
import org.techtown.find_gas_station.domain.usecase.GetLocalOilConditionUseCase
import org.techtown.find_gas_station.domain.usecase.UpdateLocalOilConditionUseCase
import javax.inject.Inject

@HiltViewModel
class SetViewModel @Inject constructor(
    private val getLocalOilConditionUseCase: GetLocalOilConditionUseCase,
    private val updateLocalOilCondition: UpdateLocalOilConditionUseCase
) : ViewModel() {

    private val _roomDbOilCondition = MutableStateFlow(
        OilCondition(
            "1000",
            "1",
            "B027"
        )
    )
    val roomDbOilCondition = _roomDbOilCondition.filterNotNull().stateIn(
        initialValue = OilCondition("1000", "1", "B027"),
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
            _roomDbOilCondition.value = getLocalOilConditionUseCase.invoke() ?: OilCondition(
                oilKind = "B027",
                sort = "1",
                radius = "1000"
            )
        }
    }

    fun updateLocalOilCondition(oilCondition: OilCondition) {
        viewModelScope.launch(Dispatchers.IO) {
            updateLocalOilCondition.invoke(oilCondition)
            _updateComplete.value = true
        }
    }
}
