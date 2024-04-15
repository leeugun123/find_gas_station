package org.techtown.find_gas_station.presentation.oilroundinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.techtown.find_gas_station.Data.TotalOilInfo
import org.techtown.find_gas_station.presentation.oilroundinfo.OilCondition
import org.techtown.find_gas_station.repository.GetOilRepository
import javax.inject.Inject

@HiltViewModel
class OilInfoViewModel @Inject constructor(
    private val getOilRepository: GetOilRepository
) : ViewModel() {

    var oilCondition = OilCondition("1000", "1", "B027")

    var processing: Boolean = false
    var sortText = ""

    private val _oilListLiveData = MutableLiveData<List<TotalOilInfo>>()
    val oilListLiveData: LiveData<List<TotalOilInfo>> get() = _oilListLiveData

    fun requestOilList(wgsX: String, wgsY: String, katecX: String, katecY: String) {

        viewModelScope.launch(Dispatchers.IO) {
            getOilRepository.requestOilList(
                wgsX,
                wgsY,
                katecX,
                katecY,
                oilCondition.radius,
                oilCondition.sort,
                oilCondition.oilKind
            )
            withContext(Dispatchers.Main) {
                _oilListLiveData.value = getOilRepository.getOilList()
            }
        }

    }

}