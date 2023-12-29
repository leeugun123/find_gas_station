package org.techtown.find_gas_station.Data.OilList

import com.google.gson.annotations.SerializedName

data class GasStationInfoResult(

    @SerializedName("RESULT")
    val oilInfoListResult : Result

)

