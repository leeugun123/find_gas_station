package org.techtown.find_gas_station.domain.areaprice.model

import com.google.gson.annotations.SerializedName
import org.techtown.find_gas_station.domain.areaprice.model.OilAveragePriceInfo

data class Result(

    @SerializedName("OIL")
    val oilAveragePriceInfo : List<OilAveragePriceInfo>
)
