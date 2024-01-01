package org.techtown.find_gas_station.Data.kakao.Response

import com.google.gson.annotations.SerializedName

data class DirectionResponse(

    @SerializedName("trans_id")
    var transId : String,

    @SerializedName("routes")
    var routes : List<Route>

)