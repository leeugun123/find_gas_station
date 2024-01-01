package org.techtown.find_gas_station.Data

data class TotalOilInfo(

    private val uid : String,
    private val oilName : String,
    private val price: String,
    private val distance: String,
    private val oilKind: String,
    private val image : Int,

    private val wgs84X : Float,
    private val wgs84Y : Float,

    private val carWash : String,
    private val conStore : String,
    private val lotNumberAdd : String,
    private val roadAdd : String,
    private val tel : String,
    private val sector : String, //업종구분
    private var actDistance : String,
    private var spendTime : String

){

    fun getUid() = uid

    fun getDistance() = distance

    fun getPrice() = price

    fun getActDistance() = actDistance

    fun getSpendTime() = spendTime

    fun getWgs84X() = wgs84X

    fun getWgs84Y() = wgs84Y

    fun setActDistance(actDistance : String){this.actDistance = actDistance}

    fun setSpendTime(spendTime : String){this.spendTime = spendTime}


}
