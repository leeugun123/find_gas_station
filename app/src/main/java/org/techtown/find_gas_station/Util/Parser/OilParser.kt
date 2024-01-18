package org.techtown.find_gas_station.Util.Parser

import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.CAR_BUTANE
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.CAR_BUTANE_ENGLISH
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.CHECK_FOUR_SPEND_TIME
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.CHECK_PRICE_CONDITION
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.CHECK_THREE_ROAD_DISTANCE
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.CHECK_TWO_DIRECT_DISTANCE
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.DIRECT_DISTANCE_GUIDE
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.FIVE_KM
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.FIVE_KM_IN_MITERS
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.GASOLINE_GUIDE
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.GASOLINE_GUIDE_ENGLISH
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.INDOOR_KEROSENE
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.INDOOR_KEROSENE_ENGLISH
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.ONE_KM
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.ONE_KM_IN_METERS
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.PREMIUM_GASOLINE
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.PREMIUM_GASOLINE_ENGLISH
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.PRICE_CONDITION_GUIDE
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.ROAD_DISTANCE_GUIDE
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.SPEND_TIME_GUIDE
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.THREE_KM
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.THREE_KM_IN_METERS
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.VIA_GUIDE
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.VIA_GUIDE_ENGLISH

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
        GASOLINE_GUIDE -> GASOLINE_GUIDE_ENGLISH
        VIA_GUIDE -> VIA_GUIDE_ENGLISH
        PREMIUM_GASOLINE -> PREMIUM_GASOLINE_ENGLISH
        INDOOR_KEROSENE -> INDOOR_KEROSENE_ENGLISH
        CAR_BUTANE-> CAR_BUTANE_ENGLISH
        else -> name
    }



}