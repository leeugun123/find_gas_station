package org.techtown.find_gas_station.Adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.kakao.kakaonavi.KakaoNaviParams
import com.kakao.kakaonavi.KakaoNaviService
import com.kakao.kakaonavi.NaviOptions
import com.kakao.kakaonavi.options.CoordType
import com.kakao.kakaonavi.options.RpOption
import com.kakao.kakaonavi.options.VehicleType
import com.kakao.sdk.navi.Constants.WEB_NAVI_INSTALL
import com.kakao.sdk.navi.NaviClient
import org.techtown.find_gas_station.Data.TotalOilInfo
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.View.Activity.OilDetailActivity
import org.techtown.find_gas_station.databinding.ItemRecyclerviewBinding

class OilInfoAdapter(private val oilInfoList : List<TotalOilInfo>, private val googleMap : GoogleMap, private val sort : String) : RecyclerView.Adapter<OilInfoAdapter.ViewHolder>() {

    inner class ViewHolder(val binding : ItemRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                = ViewHolder(ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context) , parent, false))

    override fun getItemCount() = oilInfoList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder : OilInfoAdapter.ViewHolder, position : Int) {

        val oilInfo = oilInfoList[position]

        holder.binding.name.text = oilInfo.name
        holder.binding.price.text = oilInfo.price + "원"

        when (sort) {
            "3" -> {
                holder.binding.distance.text = changeKm(oilInfo.actDistance) + "km"
            }
            "4" -> {
                holder.binding.distance.text = formatSeconds(oilInfo.spendTime.toInt())
            }
            else -> {
                holder.binding.distance.text = changeKm(oilInfo.distance) + "km"
            }
        }

        holder.binding.oilKind.text = oilInfo.oilKind
        holder.binding.oilImage.setImageResource(oilInfo.image)

        if(oilInfo.carWash == "Y"){
            holder.binding.carWashStore!!.setImageResource(R.drawable.car_wash)
        }else
            holder.binding.carWashStore!!.setImageResource(R.color.white)

        if(oilInfo.conStore == "Y"){
            holder.binding.conStore!!.setImageResource(R.drawable.convenstore)
        }else
            holder.binding.conStore!!.setImageResource(R.color.white)

        holder.binding.root.setOnClickListener{
            navigateToLocation(oilInfo.wgs84Y.toDouble(), oilInfo.wgs84X.toDouble())
        }

        addMarkerToMap(oilInfo , holder)

        holder.binding.naviButtonKakao.setOnClickListener {

            if (NaviClient.instance.isKakaoNaviInstalled(holder.itemView.context)) {

                val destination = com.kakao.kakaonavi.Location.newBuilder(
                    oilInfo.name,
                    oilInfo.wgs84X.toDouble(),
                    oilInfo.wgs84Y.toDouble()
                ).build()


                val options = NaviOptions.newBuilder().setCoordType(CoordType.WGS84).setVehicleType(VehicleType.FIRST)
                                .setRpOption(RpOption.FAST).build()

                val params = KakaoNaviParams.newBuilder(destination)
                    .setNaviOptions(options)
                    .build()

                KakaoNaviService.getInstance().navigate(holder.itemView.context, params)

            } else {

                holder.itemView.context.startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse(WEB_NAVI_INSTALL)).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )

            }

        }

        holder.binding.intelButton!!.setOnClickListener {

            val intent = Intent(holder.itemView.context, OilDetailActivity::class.java)
            intent.putExtra("oilDetailInfo",oilInfo)
            holder.itemView.context.startActivity(intent)

        }


    }

    private fun navigateToLocation(wgsY: Double, wgsX: Double) {
        googleMap.animateCamera(
            CameraUpdateFactory.newLatLng(LatLng(wgsY, wgsX)),
            600,
            null
        )
    }

    private fun addMarkerToMap(oilInfo : TotalOilInfo , holder : OilInfoAdapter.ViewHolder) {

        val pos = LatLng(oilInfo.wgs84Y.toDouble(), oilInfo.wgs84X.toDouble())
        val bitmapDraw = holder.binding.oilImage.resources.getDrawable(oilInfo.image) as BitmapDrawable
        val smallMarker = Bitmap.createScaledBitmap(bitmapDraw.bitmap, 80, 80, false)
        val markerOptions = MarkerOptions()

        markerOptions.position(pos)
            .title(oilInfo.name)
            .snippet("현 위치로부터 거리 " + oilInfo.distance + "m")
            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))

        googleMap.addMarker(markerOptions)

    }

    private fun formatSeconds(seconds: Int) : String {

        var seconds = seconds
        require(seconds >= 0) { "초는 음수일 수 없습니다." }

        val minutes = seconds / 60
        seconds %= 60

        return if (minutes == 0) {
            seconds.toString() + "초"
        } else if (seconds == 0) {
            minutes.toString() + "분"
        } else {
            minutes.toString() + "분 " + seconds + "초"
        }


    }

    private fun changeKm(distance: String) = String.format("%.1f", distance.toDouble() / 1000)
    //m -> km 변경 메소드


}