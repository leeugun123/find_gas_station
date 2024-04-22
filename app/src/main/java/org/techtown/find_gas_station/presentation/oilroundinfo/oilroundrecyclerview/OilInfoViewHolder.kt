package org.techtown.find_gas_station.presentation.oilroundinfo.oilroundrecyclerview

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kakao.kakaonavi.KakaoNaviParams
import com.kakao.kakaonavi.KakaoNaviService
import com.kakao.kakaonavi.NaviOptions
import com.kakao.kakaonavi.options.CoordType
import com.kakao.kakaonavi.options.RpOption
import com.kakao.kakaonavi.options.VehicleType
import com.kakao.sdk.navi.Constants.WEB_NAVI_INSTALL
import com.kakao.sdk.navi.NaviClient
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.data.TotalOilInfo
import org.techtown.find_gas_station.databinding.ItemRecyclerviewBinding

class OilInfoViewHolder(
    parent: ViewGroup
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_recyclerview, parent, false)
) {

    private val binding = ItemRecyclerviewBinding.bind(itemView)

    fun bind(totalOilInfo: TotalOilInfo, sort: String) {

        binding.gasStationInfo = totalOilInfo

        when (sort) {
            "3" -> {
                binding.distance.text = changeKm(totalOilInfo.actDistance) + "km"
            }

            "4" -> {
                binding.distance.text = formatSeconds(totalOilInfo.spendTime.toInt())
            }

            else -> {
                binding.distance.text = changeKm(totalOilInfo.distance) + "km"
            }
        }

        binding.oilImage.setImageResource(totalOilInfo.image)

        if (totalOilInfo.carWash == "Y")
            binding.carWashStore.setImageResource(R.drawable.car_wash)
        else
            binding.carWashStore.setImageResource(R.color.white)

        if (totalOilInfo.conStore == "Y")
            binding.conStore.setImageResource(R.drawable.convenstore)
        else
            binding.conStore.setImageResource(R.color.white)

        binding.naviButtonKakao.setOnClickListener {

            if (NaviClient.instance.isKakaoNaviInstalled(itemView.context)) {

                val destination = com.kakao.kakaonavi.Location.newBuilder(
                    totalOilInfo.name,
                    totalOilInfo.wgs84X.toDouble(),
                    totalOilInfo.wgs84Y.toDouble()
                ).build()


                val options = NaviOptions.newBuilder().setCoordType(CoordType.WGS84)
                    .setVehicleType(VehicleType.FIRST)
                    .setRpOption(RpOption.FAST).build()

                val params = KakaoNaviParams.newBuilder(destination)
                    .setNaviOptions(options)
                    .build()

                KakaoNaviService.getInstance().navigate(itemView.context, params)

            } else {
                itemView.context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(WEB_NAVI_INSTALL)
                    ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
            }
        }

        binding.intelButton.setOnClickListener {
            // TODO("OilDetailActivity 프래그먼트로 이동하는 로직 구현")
        }

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
}