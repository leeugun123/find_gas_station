package org.techtown.find_gas_station.data.repository

import kotlinx.coroutines.flow.Flow
import org.techtown.find_gas_station.data.datasource.OilAvgRemoteDataSource
import org.techtown.find_gas_station.data.remote.model.oilavg.OilAveragePriceInfo

class OilAvgRepository() {

    private val oilAvgRemoteDataSource = OilAvgRemoteDataSource()
    fun getOilAvg(prodCd: String) = oilAvgRemoteDataSource.getOilAvg(prodCd)
}