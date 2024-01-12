package org.techtown.find_gas_station.Util

object OilParser {

    fun calRad(rad : String) = when (rad) {
        "1km" -> "1000"
        "3km" -> "3000"
        "5km" -> "5000"
        else -> rad
    }

    fun calOilSort(sort : String) = when(sort){
        "가격순" -> "1"
        "직경 거리순" -> "2"
        else -> sort
    }

    fun calOilName(name : String) = when(name){
        "휘발유" -> "B027"
        "경유" -> "D047"
        "고급 휘발유" -> "B034"
        "실내 등유" -> "C004"
        "자동차 부탄"-> "K015"
        else -> name
    }



}