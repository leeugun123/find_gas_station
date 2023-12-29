package org.techtown.find_gas_station.Data.kakaoResponseModel.oilDetail

import com.google.gson.annotations.SerializedName

data class GasStationDetailInfoResult(

    @SerializedName("OIL")
    val gasStationDetailInfo : GasStationDetailInfo

)
