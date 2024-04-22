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

class StationInfoViewHolder(
    parent: ViewGroup,
    totalOilInfoClick: (totalOilInfo : TotalOilInfo) -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_recyclerview, parent, false)
) {

    private val binding = ItemRecyclerviewBinding.bind(itemView)

    init {
        binding.moveStationDetailBtnClick = {totalOilInfoClick(it)}
    }

    fun bind(totalOilInfo: TotalOilInfo, sort: String) {
        binding.gasStationInfo = totalOilInfo
        binding.sort = sort
        binding.moveKakaoBtnClick = { ::checkKakaoInstall.invoke(totalOilInfo) }
    }

    private fun checkKakaoInstall(totalOilInfo: TotalOilInfo) {
        if (NaviClient.instance.isKakaoNaviInstalled(itemView.context))
            moveToKakaoApp(totalOilInfo)
        else
            moveToKakaoWebView()
    }

    private fun moveToKakaoApp(totalOilInfo: TotalOilInfo) {
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
    }

    private fun moveToKakaoWebView() {
        itemView.context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(WEB_NAVI_INSTALL)
            ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
    }

}