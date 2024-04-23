package org.techtown.find_gas_station.presentation.gasdetailinfo

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.data.TotalOilInfo
import org.techtown.find_gas_station.databinding.FragmentStationDetailBinding
import org.techtown.find_gas_station.presentation.BaseFragment

class StationDetailFragment :
    BaseFragment<FragmentStationDetailBinding>(R.layout.fragment_station_detail),
    OnMapReadyCallback {

    private lateinit var detailMap: GoogleMap
    private val stationInfo : TotalOilInfo by lazy { requireArguments().getParcelable<TotalOilInfo>("stationInfo")!! }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()

        binding.callBtn.setOnClickListener {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${stationInfo.tel}")))
        }
    }

    private fun initUi() {
        initMapFragment()
        initText()
    }

    private fun initMapFragment() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.detailMap) as? SupportMapFragment
        mapFragment!!.getMapAsync(this)
    }

    private fun initText() {

        binding.gasImg.setImageResource(stationInfo.image)
        binding.lotAddress.text = stationInfo.lotNumberAdd
        binding.roadAddress.text = stationInfo.roadAdd
        binding.tel.text = stationInfo.tel

        setFeatureStatus(binding.carWash, stationInfo.carWash)
        setFeatureStatus(binding.convenStore, stationInfo.conStore)
    }

    private fun setFeatureStatus(textView: TextView, feature: String?) {
        textView.text = if (feature == "Y") "O" else "X"
        textView.setTextColor(
            if (feature == "Y") Color.parseColor("#009900") else Color.parseColor(
                "#ff0000"
            )
        )
    }

    override fun onMapReady(googleMap: GoogleMap) {

        detailMap = googleMap

        detailMap.apply {

            val initDetailMapPos =
                LatLng(stationInfo.wgs84Y.toDouble(), stationInfo.wgs84X.toDouble())
            moveCamera(CameraUpdateFactory.newLatLngZoom(initDetailMapPos, 18f))

            uiSettings.apply {
                isZoomControlsEnabled = true
                isZoomGesturesEnabled = true
                isMyLocationButtonEnabled = true
            }

        }
        initMap()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initMap() {

        val pos = LatLng(stationInfo.wgs84Y.toDouble(), stationInfo.wgs84X.toDouble())
        val bitmapDraw = binding.gasImg.resources.getDrawable(stationInfo.image) as BitmapDrawable
        val smallMarker = Bitmap.createScaledBitmap(bitmapDraw.bitmap, 120, 120, false)
        val markerOptions = MarkerOptions()

        markerOptions.position(pos)
            .title(stationInfo.name)
            .snippet("현 위치로부터 거리 " + stationInfo.distance + "m")
            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))

        detailMap.addMarker(markerOptions)
    }

}