package org.techtown.find_gas_station.View.Activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.databinding.ActivityIntelBinding

class IntelActivity : AppCompatActivity(), OnMapReadyCallback {

    private val binding by lazy {ActivityIntelBinding.inflate(layoutInflater)}
    private val title by lazy { intent.getStringExtra("title") }
    private val image by lazy { intent.getIntExtra("gas_img", R.drawable.oil_2) }
    private val lotAddress by lazy { intent.getStringExtra("lotAddress") }
    private val stAddress by lazy { intent.getStringExtra("stAddress") }
    private val tel by lazy { intent.getStringExtra("tel") }
    private val oilKind by lazy { intent.getStringExtra("oil_kind") }

    private lateinit var detailMap : GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        uiInit()

        binding.call!!.setOnClickListener {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$tel")))
        }

    }

    private fun uiInit(){
        textInit()
        mapInit()
    }

    private fun textInit() {

        binding.gasImage!!.setImageResource(image)
        binding.lotAddress.text = lotAddress
        binding.stAddress.text = stAddress
        binding.tel.text = tel

        binding.oilKind.text = when (oilKind) {
            "N" -> "주유소"
            "Y" -> "자동차 주유소"
            else -> "주유소/충전소 겸업"
        }

        setFeatureStatus(binding.carWash, intent.getStringExtra("carWash"))
        setFeatureStatus(binding.store, intent.getStringExtra("store"))
    }

    private fun setFeatureStatus(textView : TextView, feature : String?) {
        textView.text = if (feature == "Y") "O" else "X"
        textView.setTextColor(if (feature == "Y") Color.parseColor("#009900") else Color.parseColor("#ff0000"))
    }

    private fun mapInit() {

        val mapFragment = supportFragmentManager.findFragmentById(R.id.detailMap) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        val wgsY = intent.getFloatExtra("wgsY", 0f).toDouble()
        val wgsX = intent.getFloatExtra("wgsX", 0f).toDouble()
        val pos = LatLng(wgsY, wgsX)

        val bitmapDraw = binding.gasImage!!.resources.getDrawable(image) as BitmapDrawable
        val smallMarker = Bitmap.createScaledBitmap(bitmapDraw.bitmap, 120, 120, false)
        val markerOptions = MarkerOptions()

        markerOptions.position(pos)
            .title(title)
            .snippet("현 위치로부터 거리 2.4km")
            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))

        Handler().postDelayed({
            detailMap.addMarker(markerOptions)
            detailMap.animateCamera(CameraUpdateFactory.newLatLng(pos), 600, null)
        }, 500)

    }


    override fun onMapReady(googleMap: GoogleMap) {
        detailMap = googleMap
        detailMap.uiSettings.isZoomControlsEnabled = true
        detailMap.uiSettings.isZoomGesturesEnabled = true
        detailMap.animateCamera(CameraUpdateFactory.zoomTo(18f))
        detailMap.uiSettings.isMyLocationButtonEnabled = true
    }

}
