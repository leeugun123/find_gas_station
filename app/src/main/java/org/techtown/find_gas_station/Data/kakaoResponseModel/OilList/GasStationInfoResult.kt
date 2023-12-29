package org.techtown.find_gas_station.Data.kakaoResponseModel.OilList

import com.google.gson.annotations.SerializedName

data class GasStationInfoResult(

    @SerializedName("OIL")
    val oilInfoList : List<StationInfo>

)

