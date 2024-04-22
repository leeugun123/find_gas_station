package org.techtown.find_gas_station.bindingadapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import org.techtown.find_gas_station.data.TotalOilInfo

@BindingAdapter("sort" ,"totalOilInfo")
fun TextView.setDistanceText(sort: String, totalOilInfo: TotalOilInfo) {

    var distanceText = ""

    when (sort) {
        "3" -> {
            distanceText = changeKm(totalOilInfo.actDistance) + "km"
        }

        "4" -> {
            distanceText = formatSeconds(totalOilInfo.spendTime.toInt())
        }

        else -> {
            distanceText = changeKm(totalOilInfo.distance) + "km"
        }
    }

    text = distanceText
}

private fun changeKm(distance: String) = String.format("%.1f", distance.toDouble() / 1000)

private fun formatSeconds(seconds: Int): String {
    require(seconds >= 0) { "초는 음수일 수 없습니다." }

    val minutes = seconds / 60
    val leftSeconds = seconds % 60

    return if (minutes == 0) {
        "$leftSeconds 초"
    } else if (leftSeconds == 0) {
        "$minutes 분"
    } else {
        "$minutes 분 $leftSeconds 초"
    }
}