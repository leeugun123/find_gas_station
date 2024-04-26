package org.techtown.find_gas_station.data.kakao.Response

import com.google.gson.annotations.SerializedName

data class Summary(

    @SerializedName("distance")
    val distance : String,

    @SerializedName("duration")
    val duration : String

)