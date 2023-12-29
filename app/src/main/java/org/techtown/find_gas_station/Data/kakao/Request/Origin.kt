package org.techtown.find_gas_station.Data.kakao.Request

import com.google.gson.annotations.SerializedName

class Origin(

    @SerializedName("x")
    private val x: Double,

    @SerializedName("y")
    private val y: Double

)