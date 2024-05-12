package org.techtown.find_gas_station.data.remote.model.station.kakao.request

import com.google.gson.annotations.SerializedName

class Destination(

    @SerializedName("key")
    val key: String,

    @SerializedName("x")
    val x: Double,

    @SerializedName("y")
    val y: Double

)