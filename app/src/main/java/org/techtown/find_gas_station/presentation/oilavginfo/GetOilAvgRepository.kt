package org.techtown.find_gas_station.presentation.oilavginfo

import org.techtown.find_gas_station.Data.oilAvg.OilAveragePriceInfo
import org.techtown.find_gas_station.util.api.ApiKey
import org.techtown.find_gas_station.util.api.Api_Instance

class GetOilAvgRepository(){

    private var oilAvgList : MutableList<OilAveragePriceInfo> = mutableListOf()

    suspend fun getOilAvg(prodcd : String) : MutableList<OilAveragePriceInfo> {

        val response = Api_Instance.opiRetrofitApi.getAvgRecentPrice(ApiKey.OPI_API_KEY , "json", prodcd)

        if (response.isSuccessful)
            oilAvgList = response.body()!!.oilAveragePriceInfoResult.oilAveragePriceInfo.toMutableList()


       return oilAvgList

    }








}