package org.techtown.find_gas_station.data.remote.model.oilavg

import com.google.gson.annotations.SerializedName

data class Result(

    @SerializedName("OIL")
    val oilAveragePriceInfo : List<OilAveragePriceInfo>
)
