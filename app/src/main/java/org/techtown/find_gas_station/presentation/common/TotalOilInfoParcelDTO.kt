package org.techtown.find_gas_station.presentation.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TotalOilInfoParcelDTO(
    var uid: String = "",
    var name: String = "",
    var price: String = "",
    var directDistance: String = "1000",
    var oilKind: String = "",
    var image: Int = 0,

    var wgs84X: Float = 0f,
    var wgs84Y: Float = 0f,

    var carWash: String = "",
    var conStore: String = "",
    var lotNumberAdd: String = "",
    var roadAdd: String = "",
    var tel: String = "",
    var sector: String = "", //업종구분
    var actualDistance: String = "1000",
    var spendTime: String = "1000"
): Parcelable