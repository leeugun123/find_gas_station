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
import org.techtown.find_gas_station.data.toParableDTO
import org.techtown.find_gas_station.databinding.ItemRecyclerviewBinding
import org.techtown.find_gas_station.domain.model.StationDetailInfo
import org.techtown.find_gas_station.presentation.common.TotalOilInfoParcelDTO

class StationInfoViewHolder(
    parent: ViewGroup,
    totalOilInfoClick: (stationDetailInfo: TotalOilInfoParcelDTO) -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_recyclerview, parent, false)
) {
    private val binding = ItemRecyclerviewBinding.bind(itemView)
    
    init {
        binding.moveStationDetailBtnClick = { clickedStationDetailInfo ->
            totalOilInfoClick(clickedStationDetailInfo)
        }
    }

    fun bind(stationDetailInfo: StationDetailInfo, sort: String) {
        binding.stationDetailInfo = stationDetailInfo.toParableDTO()
        binding.sort = sort
        binding.moveKakaoBtnClick = { ::checkKakaoNaviAppInstalled.invoke(stationDetailInfo) }
    }

    private fun checkKakaoNaviAppInstalled(stationDetailInfo: StationDetailInfo) {
        if (NaviClient.instance.isKakaoNaviInstalled(itemView.context))
            moveToKakaoApp(stationDetailInfo)
        else
            moveToKakaoWebView()
    }

    private fun moveToKakaoApp(stationDetailInfo: StationDetailInfo) {
        val destination = Location(
            stationDetailInfo.name,
            stationDetailInfo.wgs84X.toString(),
            stationDetailInfo.wgs84Y.toString()
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