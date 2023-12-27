package org.techtown.find_gas_station.Data.kakaoResponseModel

data class OilTotalList(

    private val uid : String,
    private val oilName : String,
    private val price: String,
    private val distance: String,
    private val oilKind: String,
    private val image : Int,

    private val wgs84X : Float,
    private val wgs84Y : Float,

    private val carWash : String, //편의점 유무
    private val conStore : String, //지번 주소
    private val lotNumberAdd : String, //도로명 주소
    private val roadAdd : String, // 전화번호
    private val tel : String, // 업종 구분
    private val sector : String, // 실제 거리
    private var actDistance : String, //소요시간
    private var spendTime : String

){

    fun getDistance() = distance

    fun getPrice() = price

    fun getActDistance() = actDistance

    fun getSpendTime() = spendTime

}
