package org.techtown.find_gas_station.Data

data class TotalOilInfo(

    val uid : String,
    val name : String,
    val price: String,
    val distance: String,
    val oilKind : String,
    val image : Int,

    val wgs84X : Float,
    val wgs84Y : Float,

    val carWash : String,
    val conStore : String,
    val lotNumberAdd : String,
    val roadAdd : String,
    val tel : String,
    val sector : String, //업종구분
    var actDistance : String,
    var spendTime : String

){



}
