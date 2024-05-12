package org.techtown.find_gas_station.data.remote.model.station.oilDetail

import com.google.gson.annotations.SerializedName

data class Result(

    @SerializedName("OIL")
    val gasStationDetailInfo : List<GasStationDetailInfo>

)
