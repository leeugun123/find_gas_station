package org.techtown.find_gas_station

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.techtown.find_gas_station.Data.TotalOilInfo
import org.techtown.find_gas_station.repository.GetOilRepository
import javax.inject.Inject

@HiltViewModel
class OilInfoViewModel @Inject constructor(
    private val getOilRepository: GetOilRepository
) : ViewModel() {

    var oilConditionList = mutableListOf("1000", "1", "B027")

    var processing : Boolean = false
    var sortText = ""

    private val _oilListLiveData = MutableLiveData<List<TotalOilInfo>>()
    val oilListLiveData: LiveData<List<TotalOilInfo>> get() = _oilListLiveData

    fun requestOilList(xPos: String, yPos: String) {

        viewModelScope.launch(Dispatchers.IO) {
            getOilRepository.requestOilList(xPos, yPos, oilConditionList[0], oilConditionList[1], oilConditionList[2])
            withContext(Dispatchers.Main) {
                _oilListLiveData.value = getOilRepository.getOilList()
            }
        }

    }

}