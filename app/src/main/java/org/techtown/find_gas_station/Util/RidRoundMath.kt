package org.techtown.find_gas_station.Util

import kotlin.math.roundToInt

object RidRoundMath {

    fun roundStringToInteger(input: String): Int {

        return try {

            val doubleValue = input.toDouble()
            val roundedValue = doubleValue.roundToInt()

            roundedValue
        } catch (e: NumberFormatException) {
            0
        }

    }

}