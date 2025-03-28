package org.techtown.find_gas_station.presentation.ui.oilroundinfo.oilroundrecyclerview

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kakao.sdk.navi.Constants.WEB_NAVI_INSTALL
import com.kakao.sdk.navi.NaviClient
import com.kakao.sdk.navi.model.CoordType
import com.kakao.sdk.navi.model.Location
import com.kakao.sdk.navi.model.NaviOption
import com.kakao.sdk.navi.model.RpOption
import com.kakao.sdk.navi.model.VehicleType
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.databinding.ItemRecyclerviewBinding
import org.techtown.find_gas_station.domain.model.TotalOilInfo

class StationInfoViewHolder(
    parent: ViewGroup,
    totalOilInfoClick: (totalOilInfo: TotalOilInfo) -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_recyclerview, parent, false)
) {
    private val binding = ItemRecyclerviewBinding.bind(itemView)

    init {
        binding.moveStationDetailBtnClick = { totalOilInfoClick(it) }
    }

    fun bind(totalOilInfo: TotalOilInfo, sort: String) {
        binding.gasStationInfo = totalOilInfo
        binding.sort = sort
        binding.moveKakaoBtnClick = { ::checkKakaoInstalled.invoke(totalOilInfo) }
    }

    private fun checkKakaoInstalled(totalOilInfo: TotalOilInfo) {
        if (NaviClient.instance.isKakaoNaviInstalled(itemView.context))
            moveToKakaoApp(totalOilInfo)
        else
            moveToKakaoWebView()
    }

    private fun moveToKakaoApp(totalOilInfo: TotalOilInfo) {
        val destination = Location(
            totalOilInfo.name,
            totalOilInfo.wgs84X.toString(),
            totalOilInfo.wgs84Y.toString()
        )

        itemView.context.startActivity(
            NaviClient.instance.navigateIntent(
                destination,
                NaviOption(
                    coordType = CoordType.WGS84,
                    vehicleType = VehicleType.FIRST,
                    rpOption = RpOption.FAST
                )
            )
        )
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