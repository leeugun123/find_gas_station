package org.techtown.find_gas_station.data.repository

import org.techtown.find_gas_station.data.datasource.OilAvgRemoteDataSource
import org.techtown.find_gas_station.data.remote.model.oilavg.OilAveragePriceInfo

class OilAvgRepository() {

    private val remoteDataSource = OilAvgRemoteDataSource()
    private var oilAvgList: MutableList<OilAveragePriceInfo> = mutableListOf()

    suspend fun getOilAvg(prodCd: String): List<OilAveragePriceInfo> {
        oilAvgList = remoteDataSource.getOilAvg(prodCd).toMutableList()
        return oilAvgList
    }
}