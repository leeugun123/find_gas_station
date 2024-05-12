package org.techtown.find_gas_station.data.remote.model.station.kakao.request

import com.google.gson.annotations.SerializedName

data class DirectionRequest(

    @SerializedName("origin")
    private val origin: Origin,

    @SerializedName("destinations")
    private val destinations: Array<Destination?>,

    @SerializedName("radius")
    private val radius: Int


)