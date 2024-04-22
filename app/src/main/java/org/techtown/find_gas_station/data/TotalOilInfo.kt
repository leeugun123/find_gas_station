package org.techtown.find_gas_station.data

import java.io.Serializable

data class TotalOilInfo(

    var uid: String = "",
    var name: String = "",
    var price: String = "",
    var distance: String = "1000",
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
    var actDistance: String = "1000",
    var spendTime: String = "1000"

)
