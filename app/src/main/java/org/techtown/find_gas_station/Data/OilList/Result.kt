package org.techtown.find_gas_station.Data.OilList

import com.google.gson.annotations.SerializedName

data class Result(

    @SerializedName("OIL")
    val oilInfoList : List<StationInfo>

)
