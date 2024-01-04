package org.techtown.find_gas_station.Data.oilAvg

import com.google.gson.annotations.SerializedName

data class OilAveragePriceInfo(

    @SerializedName("DATE")
    private val date : String,

    @SerializedName("PRODCD")
    private val oilKind : String,

    @SerializedName("PRICE")
    private val oilPrice : String

){

    fun getDate() = date

    fun getOilKind() = oilKind

    fun getOilPrice() = oilPrice

}
