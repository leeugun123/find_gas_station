package org.techtown.find_gas_station.presentation.ui.oilavginfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.techtown.find_gas_station.data.remote.model.oilavg.OilAveragePriceInfo
import org.techtown.find_gas_station.presentation.di.RepositoryModule

class OilAvgViewModel() : ViewModel() {

    private var _oilAvgInfo = MutableStateFlow<List<OilAveragePriceInfo>>(emptyList())
    val oilAvgInfo = _oilAvgInfo.stateIn(
        initialValue = listOf(),
        started = SharingStarted.WhileSubscribed(5_000),
        scope = viewModelScope
    )

    private var getOilAvgRepository = RepositoryModule.provideGetOilAvgRepository()

    fun requestOilAvg(prodcd: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _oilAvgInfo.value = getOilAvgRepository.getOilAvg(prodcd)
        }
    }
}