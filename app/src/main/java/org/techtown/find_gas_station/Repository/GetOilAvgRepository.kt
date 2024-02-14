package org.techtown.find_gas_station.Repository

import android.app.Application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.techtown.find_gas_station.Data.TotalOilInfo
import org.techtown.find_gas_station.Data.oilAvg.OilAveragePriceInfo
import org.techtown.find_gas_station.Util.Api.ApiKey
import org.techtown.find_gas_station.Util.Api.Api_Instance

class GetOilAvgRepository(application: Application){

    private var oilAvgList : MutableList<OilAveragePriceInfo> = mutableListOf()

    fun getOilAvgList() = oilAvgList

    suspend fun requestOilAvg(prodcd : String) {

        try {

            val response = withContext(Dispatchers.IO) {
                Api_Instance.opiRetrofitApi.getAvgRecentPrice(ApiKey.OPI_API_KEY , "json", prodcd)
            }

            if (response.isSuccessful)
                oilAvgList = response.body()!!.oilAveragePriceInfoResult.oilAveragePriceInfo.toMutableList()


        } catch (_: Exception) { }

    }








}