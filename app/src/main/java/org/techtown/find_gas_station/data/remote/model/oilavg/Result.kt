package org.techtown.find_gas_station.data.remote.model.oilavg

import com.google.gson.annotations.SerializedName

data class OilAveragePriceInfoResult (
    @SerializedName("RESULT")
    val oilAveragePriceInfoResult : Result
)

data class Result(
    @SerializedName("OIL")
    val oilAveragePriceInfoDtoList: List<OilAveragePriceInfoDto>
)

data class OilAveragePriceInfoDto(
    @SerializedName("DATE")
    val date : String,

    @SerializedName("PRODCD")
    val oilKind : String,

    @SerializedName("PRICE")
    val oilPrice : String
)
