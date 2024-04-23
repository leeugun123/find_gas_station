package org.techtown.find_gas_station.presentation.oilavginfo

import org.techtown.find_gas_station.data.oilAvg.OilAveragePriceInfo
import org.techtown.find_gas_station.util.api.ApiInstance
import org.techtown.find_gas_station.util.api.ApiKey

class GetOilAvgRepository() {

    private var oilAvgList: MutableList<OilAveragePriceInfo> = mutableListOf()

    suspend fun getOilAvg(prodCd: String): MutableList<OilAveragePriceInfo> {

        val response =
            ApiInstance.opiRetrofitApi.getAvgRecentPrice(ApiKey.OPI_API_KEY, "json", prodCd)

        if (response.isSuccessful)
            oilAvgList =
                response.body()!!.oilAveragePriceInfoResult.oilAveragePriceInfo.toMutableList()


        return oilAvgList
    }
}