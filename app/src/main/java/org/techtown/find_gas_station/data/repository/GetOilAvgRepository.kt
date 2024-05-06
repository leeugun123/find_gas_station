package org.techtown.find_gas_station.data.repository

import org.techtown.find_gas_station.BuildConfig
import org.techtown.find_gas_station.domain.model.oilAvg.OilAveragePriceInfo
import org.techtown.find_gas_station.presentation.di.ApiModule

class GetOilAvgRepository() {

    private var oilAvgList: MutableList<OilAveragePriceInfo> = mutableListOf()

    suspend fun getOilAvg(prodCd: String): MutableList<OilAveragePriceInfo> {

        val response =
            ApiModule.provideOpinetApi().getAvgRecentPrice(BuildConfig.GAS_API_KEY, "json", prodCd)

        if (response.isSuccessful)
            oilAvgList =
                response.body()!!.oilAveragePriceInfoResult.oilAveragePriceInfo.toMutableList()


        return oilAvgList
    }
}