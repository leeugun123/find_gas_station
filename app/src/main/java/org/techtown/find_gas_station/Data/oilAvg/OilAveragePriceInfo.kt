package org.techtown.find_gas_station.Data.oilAvg

import com.google.gson.annotations.SerializedName

data class OilAveragePriceInfo(

    @SerializedName("DATE")
    val date : String,

    @SerializedName("PRODCD")
    val oilKind : String,

    @SerializedName("PRICE")
    val oilPrice : String

)
