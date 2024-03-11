package org.techtown.find_gas_station.View.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.techtown.find_gas_station.Data.TotalOilInfo
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.databinding.ActivityIntelBinding

class OilDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private val oilInfoData by lazy { intent.getSerializableExtra("oilDetailInfo") as TotalOilInfo}
    private val binding by lazy { ActivityIntelBinding.inflate(layoutInflater)}

    private lateinit var detailMap : GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        uiInit()

        binding.call!!.setOnClickListener {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${oilInfoData.tel}")))
        }

    }

    private fun uiInit() {
        mapFragmentInit()
        textInit()
    }

    private fun mapFragmentInit() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.detailMap) as? SupportMapFragment
        mapFragment!!.getMapAsync(this)
    }


    private fun textInit() {

        binding.gasImage!!.setImageResource(oilInfoData.image)
        binding.lotAddress.text = oilInfoData.lotNumberAdd
        binding.stAddress.text = oilInfoData.roadAdd
        binding.tel.text = oilInfoData.tel

        binding.oilKind.text = when (oilInfoData.oilKind) {
            "N" -> "주유소"
            "Y" -> "자동차 주유소"
            else -> "주유소/충전소 겸업"
        }

        setFeatureStatus(binding.carWash, oilInfoData.carWash)
        setFeatureStatus(binding.store, oilInfoData.conStore)
    }

    private fun setFeatureStatus(textView : TextView, feature : String?) {

        textView.text = if (feature == "Y") "O" else "X"
        textView.setTextColor(if (feature == "Y") Color.parseColor("#009900") else Color.parseColor("#ff0000"))
    }

    override fun onMapReady(googleMap: GoogleMap) {

        detailMap = googleMap

        detailMap.apply {

            val initDetailMapPos = LatLng(oilInfoData.wgs84Y.toDouble(), oilInfoData.wgs84X.toDouble())
            moveCamera(CameraUpdateFactory.newLatLngZoom(initDetailMapPos, 18f))

            uiSettings.apply {
                isZoomControlsEnabled = true
                isZoomGesturesEnabled = true
                isMyLocationButtonEnabled = true
            }

        }


        mapInit()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun mapInit() {

        val pos = LatLng(oilInfoData.wgs84Y.toDouble(), oilInfoData.wgs84X.toDouble())
        val bitmapDraw = binding.gasImage!!.resources.getDrawable(oilInfoData.image) as BitmapDrawable
        val smallMarker = Bitmap.createScaledBitmap(bitmapDraw.bitmap, 120, 120, false)
        val markerOptions = MarkerOptions()

        markerOptions.position(pos)
            .title(oilInfoData.name)
            .snippet("현 위치로부터 거리 " + oilInfoData.distance + "m")
            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))

        detailMap.addMarker(markerOptions)

    }

}
