package org.techtown.find_gas_station.data.remote.model.station.kakao

import com.google.gson.annotations.SerializedName

data class DirectionRequest(

    @SerializedName("origin")
    private val origin: Origin,

    @SerializedName("destinations")
    private val destinations: Array<Destination?>,

    @SerializedName("radius")
    private val radius: Int
)

class Origin(
    @SerializedName("x")
    private val x : Double,

    @SerializedName("y")
    private val y : Double
)

class Destination(
    @SerializedName("key")
    val key: String,

    @SerializedName("x")
    val x: Double,

    @SerializedName("y")
    val y: Double
)