package org.techtown.find_gas_station.data.datasource

import org.techtown.find_gas_station.BuildConfig
import org.techtown.find_gas_station.data.remote.model.oilavg.OilAveragePriceInfo
import org.techtown.find_gas_station.presentation.di.ApiModule

class OilAvgRemoteDataSource() {

    private val apiService = ApiModule.provideOpinetApi()

    suspend fun getOilAvg(prodCd: String): List<OilAveragePriceInfo> {
        val response = apiService.getAvgRecentPrice(BuildConfig.GAS_API_KEY, "json", prodCd)
        if (response.isSuccessful) {
            return response.body()!!.oilAveragePriceInfoResult.oilAveragePriceInfo
        } else {
            throw Exception("Failed to fetch data")
        }
    }
}