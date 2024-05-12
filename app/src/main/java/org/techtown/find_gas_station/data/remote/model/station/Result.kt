package org.techtown.find_gas_station.data.remote.model.station

import com.google.gson.annotations.SerializedName

data class Result(

    @SerializedName("OIL")
    val oilInfoList : List<StationInfo>

)
