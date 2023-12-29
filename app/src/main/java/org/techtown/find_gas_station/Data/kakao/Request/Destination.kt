package org.techtown.find_gas_station.Data.kakao.Request

import com.google.gson.annotations.SerializedName

class Destination(

    @SerializedName("key")
    val key: String,

    @SerializedName("x")
    val x: Double,

    @SerializedName("y")
    val y: Double

)