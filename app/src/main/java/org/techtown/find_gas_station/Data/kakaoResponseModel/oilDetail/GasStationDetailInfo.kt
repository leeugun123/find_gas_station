package org.techtown.find_gas_station.Data.kakaoResponseModel.oilDetail

import com.google.gson.annotations.SerializedName

data class GasStationDetailInfo(

    @SerializedName("UNI_ID")
    val uniqueId : String,

    @SerializedName("POLL_DIV_CO")
    val brandName : String,

    @SerializedName("OS_NM")
    val stationName : String,

    @SerializedName("VAN_ADR")
    val address : String,

    @SerializedName("NEW_ADR")
    val streetAddress : String,

    @SerializedName("TEL")
    val calNumber : String,

    @SerializedName("CAR_WASH_YN")
    val carWashExist : String,

    @SerializedName("CVS_YN")
    val conStoreExist : String,

    @SerializedName("GIS_X_COOR")
    val gisX : String,

    @SerializedName("GIS_Y_COOR")
    val gisY : String

)
