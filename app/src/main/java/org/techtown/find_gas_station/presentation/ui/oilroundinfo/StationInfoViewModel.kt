package org.techtown.find_gas_station.presentation.ui.oilroundinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.techtown.find_gas_station.domain.stationsInfo.model.TotalOilInfo
import org.techtown.find_gas_station.data.repository.module.RepositoryModule

class StationInfoViewModel() : ViewModel() {

    val isLoading = MutableStateFlow(false)

    private val _emptyCheck = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val emptyCheck: StateFlow<Boolean>
        get() = _emptyCheck
            .flatMapLatest { value ->
                flow {
                    if (value && !isLoading.value) {
                        emit(value)
                    }
                }
            }.stateIn(
                initialValue = false,
                started = SharingStarted.WhileSubscribed(5_000),
                scope = viewModelScope
            )

    private val _oilList = MutableStateFlow<List<TotalOilInfo>>(emptyList())

    val oilList = _oilList.stateIn(
        initialValue = listOf(),
        started = SharingStarted.WhileSubscribed(5_000),
        scope = viewModelScope
    )

    var sortText = ""

    var conditionChangeFlag = true
    var oilCondition = OilCondition("1000", "1", "D047")
    var afterOilCondition = OilCondition("", "", "")

    private val stationInfoRepository = RepositoryModule.provideStationInfoRepository()

    fun requestOilList(wgsX: String, wgsY: String, katecX: String, katecY: String) {

        viewModelScope.launch(Dispatchers.IO) {

            setLoading(LoadingState.LOADING)

            stationInfoRepository.requestStationList(
                wgsX,
                wgsY,
                katecX,
                katecY,
                oilCondition.radius,
                oilCondition.sort,
                oilCondition.oilKind
            )

            val getList = stationInfoRepository.getStationList()
            _oilList.value = getList

            setLoading(LoadingState.NOT_LOADING)

            checkList(getList.size)
        }
    }

    private fun setLoading(loadingState: LoadingState) {
        when (loadingState) {
            LoadingState.LOADING -> isLoading.value = true
            LoadingState.NOT_LOADING -> isLoading.value = false
        }
    }

    private fun checkList(size: Int) {
        if (size == 0)
            setEmptyCheck(true)
        else
            setEmptyCheck(false)
    }

    private fun setEmptyCheck(check: Boolean) {
        _emptyCheck.value = check
    }
}
