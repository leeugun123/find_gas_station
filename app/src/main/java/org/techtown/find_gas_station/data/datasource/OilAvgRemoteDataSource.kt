package org.techtown.find_gas_station.data.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.techtown.find_gas_station.BuildConfig
import org.techtown.find_gas_station.data.remote.model.oilavg.OilAveragePriceInfo
import org.techtown.find_gas_station.presentation.di.ApiModule

class OilAvgRemoteDataSource {

    private val opinetApiService = ApiModule.provideOpinetApi()

    suspend fun getOilAvg(prodCd: String) : List<OilAveragePriceInfo> {
        val response = opinetApiService.getAvgRecentPrice(BuildConfig.GAS_API_KEY, "json", prodCd)
        if (response.isSuccessful) {
            return response.body()!!.oilAveragePriceInfoResult.oilAveragePriceInfo
        } else {
            throw Exception("Failed to fetch data")
        }
    }
}