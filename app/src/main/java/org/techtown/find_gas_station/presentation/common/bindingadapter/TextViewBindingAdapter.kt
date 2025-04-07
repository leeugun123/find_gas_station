package org.techtown.find_gas_station.presentation.common.bindingadapter

import android.graphics.Color
import android.widget.TextView
import androidx.databinding.BindingAdapter
import org.techtown.find_gas_station.presentation.common.TotalOilInfoParcelDTO

@BindingAdapter("sort", "stationDetailInfo")
fun TextView.setDistanceText(sort: String, stationDetailInfo: TotalOilInfoParcelDTO) {

    var distanceText = ""

    when (sort) {

        "3" -> {
            if (stationDetailInfo.actualDistance.isNotEmpty())
                distanceText = changeKm(stationDetailInfo.actualDistance) + "km"
        }

        "4" -> {
            if(stationDetailInfo.spendTime.isNotEmpty())
                distanceText = formatSeconds(stationDetailInfo.spendTime.toInt())
        }

        else -> {
            if (stationDetailInfo.directDistance.isNotEmpty())
                distanceText = changeKm(stationDetailInfo.directDistance) + "km"
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

@BindingAdapter("app:setFeatureStatus")
fun TextView.setFeatureStatus(feature: String) {
    text = if (feature == "Y") "O" else "X"
    setTextColor(
        if (feature == "Y") Color.parseColor("#009900") else Color.parseColor(
            "#ff0000"
        )
    )
}