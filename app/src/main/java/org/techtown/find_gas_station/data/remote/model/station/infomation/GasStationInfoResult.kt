package org.techtown.find_gas_station.data.remote.model.station.infomation

import com.google.gson.annotations.SerializedName

data class GasStationInfoResult(
    @SerializedName("RESULT")
    val oilInfoListResult: Result
)

data class Result(
    @SerializedName("OIL")
    val oilInfoList : List<StationInfo>
)

data class StationInfo(
    @SerializedName("UNI_ID")
    val id : String,

    @SerializedName("POLL_DIV_CD")
    val pollDivCd : String,

    @SerializedName("OS_NM")
    val osName: String,

    @SerializedName("PRICE")
    val price : String,

    @SerializedName("DISTANCE")
    val distance : String ,

    @SerializedName("GIS_X_COOR")
    val gisX : String ,

    @SerializedName("GIS_Y_COOR")
    val gisY : String
)

