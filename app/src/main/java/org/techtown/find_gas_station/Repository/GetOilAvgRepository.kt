package org.techtown.find_gas_station.Repository

import android.app.Application
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.techtown.find_gas_station.Data.oilAvg.OilAveragePriceInfo
import org.techtown.find_gas_station.Data.oilAvg.OilAveragePriceInfoResult
import org.techtown.find_gas_station.Util.Api.ApiKey
import org.techtown.find_gas_station.Util.Api.Api_Instance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetOilAvgRepository(application: Application){

    private var oilAvgInfoLiveData : MutableLiveData<List<OilAveragePriceInfo>> = MutableLiveData()

    fun getOilAvgInfoLiveData() = this.oilAvgInfoLiveData

    suspend fun getOilAvg(prodcd : String) {

        try {
            val response = withContext(Dispatchers.IO) {
                Api_Instance.opiRetrofitApi.getAvgRecentPrice(ApiKey.OPI_API_KEY , "json", prodcd)
            }

            if (response.isSuccessful)
                oilAvgInfoLiveData.value = response.body()!!.oilAveragePriceInfoResult.oilAveragePriceInfo


        } catch (e: Exception) { }


    }








}