package org.techtown.find_gas_station.data.remote.model.station.kakao.response

import com.google.gson.annotations.SerializedName

data class DirectionResponse(

    @SerializedName("trans_id")
    var transId : String,

    @SerializedName("routes")
    var routes : List<Route>

)