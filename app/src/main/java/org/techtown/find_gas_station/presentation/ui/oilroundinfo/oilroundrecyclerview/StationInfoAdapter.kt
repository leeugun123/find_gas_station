package org.techtown.find_gas_station.presentation.ui.oilroundinfo.oilroundrecyclerview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.techtown.find_gas_station.domain.station.model.TotalOilInfo

class StationInfoAdapter(
    private val oilInfoList: List<TotalOilInfo>,
    private val googleMap: GoogleMap,
    private val sort: String,
    private val totalOilInfoClick: (totalOilInfo: TotalOilInfo) -> Unit,
) : RecyclerView.Adapter<StationInfoViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): StationInfoViewHolder = StationInfoViewHolder(parent , totalOilInfoClick = totalOilInfoClick)

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: StationInfoViewHolder, position: Int) {
        holder.bind(oilInfoList[position], sort)

        addMarkerToMap(oilInfoList[position], holder)

        holder.itemView.setOnClickListener {
            navigateToLocation(
                oilInfoList[position].wgs84Y.toDouble(),
                oilInfoList[position].wgs84X.toDouble()
            )
        }
    }

    private fun addMarkerToMap(oilInfo: TotalOilInfo, holder: StationInfoViewHolder) {

        val pos = LatLng(oilInfo.wgs84Y.toDouble(), oilInfo.wgs84X.toDouble())
        val bitmapDraw =
            ContextCompat.getDrawable(holder.itemView.context, oilInfo.image) as BitmapDrawable
        val smallMarker = Bitmap.createScaledBitmap(bitmapDraw.bitmap, 80, 80, false)
        val markerOptions = MarkerOptions()

        markerOptions.position(pos)
            .title(oilInfo.name)
            .snippet("현 위치로부터 거리 " + oilInfo.distance + "m")
            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))

        googleMap.addMarker(markerOptions)
    }

    private fun navigateToLocation(wgsY: Double, wgsX: Double) {
        googleMap.animateCamera(
            CameraUpdateFactory.newLatLng(LatLng(wgsY, wgsX)),
            GOOGLE_MAP_DURATION,
            null
        )
    }

    override fun getItemCount() = oilInfoList.size

    companion object {
        private const val GOOGLE_MAP_DURATION = 600
    }
}