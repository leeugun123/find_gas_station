package org.techtown.find_gas_station.util.parser

import org.techtown.find_gas_station.util.constant.ConstantOilCondition.CAR_BUTANE_KOREAN
import org.techtown.find_gas_station.util.constant.ConstantOilCondition.CAR_BUTANE_ENGLISH
import org.techtown.find_gas_station.util.constant.ConstantOilCondition.CHECK_FOUR_SPEND_TIME
import org.techtown.find_gas_station.util.constant.ConstantOilCondition.CHECK_PRICE_CONDITION
import org.techtown.find_gas_station.util.constant.ConstantOilCondition.CHECK_THREE_ROAD_DISTANCE
import org.techtown.find_gas_station.util.constant.ConstantOilCondition.CHECK_TWO_DIRECT_DISTANCE
import org.techtown.find_gas_station.util.constant.ConstantOilCondition.DIRECT_DISTANCE_GUIDE
import org.techtown.find_gas_station.util.constant.ConstantOilCondition.FIVE_KM
import org.techtown.find_gas_station.util.constant.ConstantOilCondition.FIVE_KM_IN_MITERS
import org.techtown.find_gas_station.util.constant.ConstantOilCondition.GASOLINE_KOREAN
import org.techtown.find_gas_station.util.constant.ConstantOilCondition.GASOLINE_GUIDE_ENGLISH
import org.techtown.find_gas_station.util.constant.ConstantOilCondition.INDOOR_KEROSENE_KOREAN
import org.techtown.find_gas_station.util.constant.ConstantOilCondition.INDOOR_KEROSENE_ENGLISH
import org.techtown.find_gas_station.util.constant.ConstantOilCondition.ONE_KM
import org.techtown.find_gas_station.util.constant.ConstantOilCondition.ONE_KM_IN_METERS
import org.techtown.find_gas_station.util.constant.ConstantOilCondition.PREMIUM_GASOLINE_KOREAN
import org.techtown.find_gas_station.util.constant.ConstantOilCondition.PREMIUM_GASOLINE_ENGLISH
import org.techtown.find_gas_station.util.constant.ConstantOilCondition.PRICE_CONDITION_GUIDE
import org.techtown.find_gas_station.util.constant.ConstantOilCondition.ROAD_DISTANCE_GUIDE
import org.techtown.find_gas_station.util.constant.ConstantOilCondition.SPEND_TIME_GUIDE
import org.techtown.find_gas_station.util.constant.ConstantOilCondition.THREE_KM
import org.techtown.find_gas_station.util.constant.ConstantOilCondition.THREE_KM_IN_METERS
import org.techtown.find_gas_station.util.constant.ConstantOilCondition.VIA_KOREAN
import org.techtown.find_gas_station.util.constant.ConstantOilCondition.VIA_GUIDE_ENGLISH

object OilParser {

    fun calRad(rad : String) = when (rad) {
        ONE_KM -> ONE_KM_IN_METERS
        THREE_KM -> THREE_KM_IN_METERS
        FIVE_KM -> FIVE_KM_IN_MITERS
        else -> rad
    }

    fun calOilSort(sort : String) = when(sort){
        PRICE_CONDITION_GUIDE -> CHECK_PRICE_CONDITION
        DIRECT_DISTANCE_GUIDE -> CHECK_TWO_DIRECT_DISTANCE
        ROAD_DISTANCE_GUIDE -> CHECK_THREE_ROAD_DISTANCE
        SPEND_TIME_GUIDE -> CHECK_FOUR_SPEND_TIME
        else -> sort
    }

    fun calOilName(name : String) = when(name){
        GASOLINE_KOREAN -> GASOLINE_GUIDE_ENGLISH
        VIA_KOREAN -> VIA_GUIDE_ENGLISH
        PREMIUM_GASOLINE_KOREAN -> PREMIUM_GASOLINE_ENGLISH
        INDOOR_KEROSENE_KOREAN -> INDOOR_KEROSENE_ENGLISH
        CAR_BUTANE_KOREAN-> CAR_BUTANE_ENGLISH
        else -> name
    }



}