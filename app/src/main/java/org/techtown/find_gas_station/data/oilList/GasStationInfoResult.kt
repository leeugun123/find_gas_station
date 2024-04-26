package org.techtown.find_gas_station.data.oilList

import com.google.gson.annotations.SerializedName

data class GasStationInfoResult(

    @SerializedName("RESULT")
    val oilInfoListResult: Result

)

