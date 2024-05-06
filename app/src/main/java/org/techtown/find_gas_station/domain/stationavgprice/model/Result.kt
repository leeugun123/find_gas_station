package org.techtown.find_gas_station.domain.stationavgprice.model

import com.google.gson.annotations.SerializedName

data class Result(

    @SerializedName("OIL")
    val oilAveragePriceInfo : List<OilAveragePriceInfo>
)
