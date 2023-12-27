package org.techtown.find_gas_station.Data.kakaoResponseModel.OilList

import com.google.gson.annotations.SerializedName

data class StationIntel(

    @SerializedName("UNI_ID")
    val id : String,

    @SerializedName("POLL_DIV_CD")
    val pollDivCd : String,

    @SerializedName("OS_NM")
    val osName: String,

    @SerializedName("PRICE")
    val price : Int,

    @SerializedName("DISTANCE")
    val distance : Double ,

    @SerializedName("GIS_X_COOR")
    val gisX : Double ,

    @SerializedName("GIS_Y_COOR")
    val gisY : Double


)