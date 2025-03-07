package org.techtown.find_gas_station.data.remote.model.station.kakao.response

import com.google.gson.annotations.SerializedName

data class DirectionResponse(

    @SerializedName("trans_id")
    var transId : String,

    @SerializedName("routes")
    var routes : List<Route>
)

data class Route(
    @SerializedName("result_code")
    var resultCode : String,

    @SerializedName("result_msg")
    var resultMsg : String,

    @SerializedName("key")
    var key : String,

    @SerializedName("summary")
    var summary : Summary
)

data class Summary(
    @SerializedName("distance")
    val distance : String,

    @SerializedName("duration")
    val duration : String
)