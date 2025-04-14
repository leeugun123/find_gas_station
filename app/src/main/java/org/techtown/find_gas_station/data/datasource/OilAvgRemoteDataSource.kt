package org.techtown.find_gas_station.data.datasource

import org.techtown.find_gas_station.BuildConfig
import org.techtown.find_gas_station.data.remote.api.OpinetApi
import org.techtown.find_gas_station.data.remote.model.oilavg.OilAveragePriceInfoDto
import org.techtown.find_gas_station.di.ApiModule
import javax.inject.Inject

class OilAvgRemoteDataSource (
    private val opinetApiService: OpinetApi
) {

    suspend fun getOilAvg(prodCd: String) : List<OilAveragePriceInfoDto> {
        val response = opinetApiService.getAvgRecentPrice(BuildConfig.GAS_API_KEY, "json", prodCd)
        if (response.isSuccessful) {
            return response.body()!!.oilAveragePriceInfoResult.oilAveragePriceInfoDtoList
        } else {
            throw Exception("Failed to fetch data")
        }
    }
}


