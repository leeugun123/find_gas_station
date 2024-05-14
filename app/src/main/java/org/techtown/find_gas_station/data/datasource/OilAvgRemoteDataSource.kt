package org.techtown.find_gas_station.data.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.techtown.find_gas_station.BuildConfig
import org.techtown.find_gas_station.data.remote.model.oilavg.OilAveragePriceInfo
import org.techtown.find_gas_station.presentation.di.ApiModule

class OilAvgRemoteDataSource {

    private val apiService = ApiModule.provideOpinetApi()

    fun getOilAvg(prodCd: String): Flow<List<OilAveragePriceInfo>> = flow {
        val response = apiService.getAvgRecentPrice(BuildConfig.GAS_API_KEY, "json", prodCd)
        if (response.isSuccessful) {
            emit(response.body()!!.oilAveragePriceInfoResult.oilAveragePriceInfo)
        } else {
            throw Exception("Failed to fetch data")
        }
    }
}