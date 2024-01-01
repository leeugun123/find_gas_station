package org.techtown.find_gas_station.Data.kakao.Response

import com.google.gson.annotations.SerializedName

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