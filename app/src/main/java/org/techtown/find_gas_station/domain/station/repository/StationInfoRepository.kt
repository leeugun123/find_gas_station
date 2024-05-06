package org.techtown.find_gas_station.domain.station.repository

import org.techtown.find_gas_station.data.oilList.GasStationInfoResult
import org.techtown.find_gas_station.domain.station.model.TotalOilInfo

interface StationInfoRepository {

    var tempList: MutableList<TotalOilInfo>

    var wgsX: String?
    var wgsY: String?

    fun getStationList() : List<TotalOilInfo>

    suspend fun requestStationList(
        wgsX: String,
        wgsY: String,
        katecX: String,
        katecY: String,
        radius: String,
        sort: String,
        oilKind: String
    )

    suspend fun getStationDetail(
        sort: String,
        size: Int,
        uid: String,
        name: String,
        gasPrice: String,
        distance: String,
        inputOil: String,
        imageResource: Int,
        destinationX: Float,
        destinationY: Float
    )


}