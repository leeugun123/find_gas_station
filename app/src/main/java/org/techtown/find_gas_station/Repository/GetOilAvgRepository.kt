package org.techtown.find_gas_station.Repository

import android.app.Application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.techtown.find_gas_station.Data.TotalOilInfo
import org.techtown.find_gas_station.Data.oilAvg.OilAveragePriceInfo
import org.techtown.find_gas_station.Util.Api.ApiKey
import org.techtown.find_gas_station.Util.Api.Api_Instance

class GetOilAvgRepository(){

    private var oilAvgList : MutableList<OilAveragePriceInfo> = mutableListOf()

    suspend fun getOilAvg(prodcd : String) : MutableList<OilAveragePriceInfo> {

        val response = Api_Instance.opiRetrofitApi.getAvgRecentPrice(ApiKey.OPI_API_KEY , "json", prodcd)

        if (response.isSuccessful)
            oilAvgList = response.body()!!.oilAveragePriceInfoResult.oilAveragePriceInfo.toMutableList()


       return oilAvgList

    }








}