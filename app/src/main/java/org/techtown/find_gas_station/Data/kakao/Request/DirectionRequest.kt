package org.techtown.find_gas_station.Data.kakao.Request

import com.google.gson.annotations.SerializedName

data class DirectionRequest(

    @SerializedName("origin")
    private val origin : Origin,

    @SerializedName("destinations")
    private val destinations : List<Destination>,

    @SerializedName("radius")
    private val radius : Int


)