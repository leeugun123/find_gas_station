package org.techtown.find_gas_station.Util

object OilParser {

    fun calRad(rad : String) = when (rad) {
        "1km" -> "1000"
        "3km" -> "3000"
        else -> "5000"
    }

    fun calOilSort(sort : String) = when(sort){
        "가격순" -> "1"
        else -> "2"
    }

    fun calOilName(name : String) = when(name){
        "휘발유" -> "B027"
        "경유" -> "D047"
        "고급휘발유" -> "B034"
        "실내등유" -> "C004"
        else -> "K015"
    }



}