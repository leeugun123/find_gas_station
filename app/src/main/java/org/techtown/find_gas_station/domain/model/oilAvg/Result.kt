package org.techtown.find_gas_station.domain.model.oilAvg

import com.google.gson.annotations.SerializedName

data class Result(

    @SerializedName("OIL")
    val oilAveragePriceInfo : List<OilAveragePriceInfo>
)
