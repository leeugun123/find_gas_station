package org.techtown.find_gas_station.presentation.ui.oilroundinfo.oilroundrecyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.presentation.common.TotalOilInfoParcelDTO
import org.techtown.find_gas_station.data.toParableDTO
import org.techtown.find_gas_station.domain.model.TotalOilInfo
import org.techtown.find_gas_station.databinding.ItemRecyclerviewBinding

class StationInfoViewHolder(
    parent: ViewGroup,
    totalOilInfoClick: (totalOilInfo: TotalOilInfoParcelDTO) -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_recyclerview, parent, false)
) {
    private val binding = ItemRecyclerviewBinding.bind(itemView)
    init {
        binding.moveStationDetailBtnClick = { totalOilInfoClick(it) }
    }

    fun bind(totalOilInfo: TotalOilInfo, sort: String) {
        binding.gasStationInfo = totalOilInfo.toParableDTO()
        binding.sort = sort
        binding.moveKakaoBtnClick = { ::checkKakaoInstall.invoke(totalOilInfo) }
    }

    private fun checkKakaoInstall(totalOilInfo: TotalOilInfo) {

       /* if (NaviClient.instance.isKakaoNaviInstalled(itemView.context))
            moveToKakaoApp(totalOilInfo)
        else
            moveToKakaoWebView()*/
    }

    private fun moveToKakaoApp(totalOilInfo: TotalOilInfo) {
        /*
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
        )*/
    }

    private fun moveToKakaoWebView() {
        /*
        itemView.context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(WEB_NAVI_INSTALL)
            ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )

         */
    }

}