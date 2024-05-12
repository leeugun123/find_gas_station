package org.techtown.find_gas_station.data.remote.model.station.kakao.response

import com.google.gson.annotations.SerializedName

data class Summary(

    @SerializedName("distance")
    val distance : String,

    @SerializedName("duration")
    val duration : String

)