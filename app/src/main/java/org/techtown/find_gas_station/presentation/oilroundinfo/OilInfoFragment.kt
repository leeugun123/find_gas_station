package org.techtown.find_gas_station.presentation.oilroundinfo

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.techtown.find_gas_station.BaseFragment
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.databinding.FragmentOilInfoBinding
import org.techtown.find_gas_station.util.constant.ConstantGuide
import org.techtown.find_gas_station.util.constant.ConstantsTime
import org.techtown.find_gas_station.util.gps.GeoTrans
import org.techtown.find_gas_station.util.gps.GeoTransPoint
import org.techtown.find_gas_station.util.gps.GpsTracker

@AndroidEntryPoint
class OilInfoFragment : BaseFragment<FragmentOilInfoBinding>(R.layout.fragment_oil_info),
    OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private var wgsX = ""
    private var wgsY = ""

    private val mFusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(
            requireContext()
        )
    }
    private val mapFragment by lazy { SupportMapFragment.newInstance() }
    private val locationRequest by lazy {
        LocationRequest()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(ConstantsTime.UPDATE_INTERVAL_MS.toLong())
            .setFastestInterval(ConstantsTime.FASTEST_UPDATE_INTERVAL_MS.toLong())
    }

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
        }
    }

    private lateinit var smoothScroller: LinearSmoothScroller

    private val oilInfoViewModel: OilInfoViewModel by activityViewModels()

    private lateinit var gpsTracker: GpsTracker
    private lateinit var mMap: GoogleMap

    private val requiredPermission = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        childFragmentManager.beginTransaction()
            .replace(R.id.map, mapFragment)
            .commit()

        mapFragment.getMapAsync(this)

        initSmoothScroller()
        initSetting()
        initBinding()
        requestRoundOilInfo()
        observeOilList()
    }

    private fun initSmoothScroller() {
        smoothScroller = object : LinearSmoothScroller(binding.listRecycler.context) {
            override fun getVerticalSnapPreference() = SNAP_TO_START
        }
    }

    private fun initBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.requestOilDataClick = ::getOilData
        binding.moveToSettingFragmentClick = ::moveToSettingFragment
        binding.oilInfoViewModel = oilInfoViewModel
    }

    private fun requestRoundOilInfo() {
        getOilData()
        updateSortText()
    }

    private fun getOilData() {

        activeProgressBar(true)
        initGpsTracker()
        val katecPos =
            transFormPoint(gpsTracker.getLatitude().toFloat(), gpsTracker.getLongitude().toFloat())

        oilInfoViewModel.requestOilList(
            wgsX,
            wgsY,
            katecPos.x.toString(),
            katecPos.y.toString(),
        )
    }

    private fun updateSortText() {
        oilInfoViewModel._sortText.value = when (oilInfoViewModel.oilCondition.sort) {
            "1" -> requireContext().getString(R.string.sort_price)
            "2" -> requireContext().getString(R.string.sort_direct_distance)
            "3" -> requireContext().getString(R.string.sort_road_distance)
            "4" -> requireContext().getString(R.string.sort_spend_time)
            else -> ""
        }
    }

    private fun moveToSettingFragment() {
        findNavController().navigate(R.id.action_mainFragment_to_settingFragment)
    }

    private fun observeOilList() {
        oilInfoViewModel.oilListLiveData.observe(viewLifecycleOwner) { oilList ->

            activeProgressBar(false)

            binding.listRecycler.adapter =
                OilInfoAdapter(oilList, mMap, oilInfoViewModel.oilCondition.sort)
            upRecyclerView()
            checkListEmpty(oilList.size)
        }

    }

    private fun activeProgressBar(state: Boolean) {
        oilInfoViewModel._processing.value = state
    }

    private fun upRecyclerView() {

        lifecycleScope.launch(Dispatchers.Main) {
            delay(UP_RECYCLERVIEW_TIME)
            smoothScroller.targetPosition = 0
            binding.listRecycler.layoutManager!!.startSmoothScroll(smoothScroller)
        }
    }

    private fun checkListEmpty(oilListSize: Int) {
        if (oilListSize == 0)
            showEmptyMessage()
    }

    private fun showEmptyMessage() {
        Toast.makeText(context, R.string.data_empty_message, Toast.LENGTH_SHORT).show()
    }


    private fun initSetting() {
        initWindowSet()
        initLocationRequest()
        binding.listRecycler.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    private fun initWindowSet() {
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )
    }//화면이 꺼지지 않도록 유지

    private fun initLocationRequest() {
        LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
    }

    private fun initGpsTracker() {
        gpsTracker = GpsTracker(requireContext())
    }

    private fun transFormPoint(latitude: Float, longtitude: Float): GeoTransPoint {
        val geoTransPoint = GeoTransPoint(longtitude.toDouble(), latitude.toDouble())
        wgsX = geoTransPoint.x.toString()
        wgsY = geoTransPoint.y.toString()
        return GeoTrans.convert(GeoTrans.GEO, GeoTrans.KATEC, geoTransPoint)
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        if (checkPermission()) {
            mFusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.myLooper()
            )
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        applyMap()
        if (checkPermission())
            startLocationUpdates()
        else
            handleLocationPermissionRequest()
    }

    @SuppressLint("MissingPermission")
    private fun applyMap() {

        mMap.apply {

            initGpsTracker()
            moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        gpsTracker.getLatitude(),
                        gpsTracker.getLongitude()
                    ), 15f
                )
            )
            isMyLocationEnabled = true
            animateCamera(CameraUpdateFactory.zoomTo(15f))

            uiSettings.apply {
                isZoomControlsEnabled = true
                isZoomGesturesEnabled = true
                isMyLocationButtonEnabled = true
            }
            setOnMapClickListener(GoogleMap.OnMapClickListener { })
        }

    }

    private fun handleLocationPermissionRequest() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                requiredPermission[0]
            )
        ) {
            Snackbar.make(
                binding.layoutMain,
                ConstantGuide.REQUIRE_LOCATION_PERMISSION_GUIDE,
                Snackbar.LENGTH_INDEFINITE
            ).setAction(ConstantGuide.CONFIRM_GUIDE) {
                ActivityCompat.requestPermissions(
                    requireActivity(), requiredPermission,
                    ConstantsTime.PERMISSIONS_REQUEST_CODE
                )
            }.show()
        } else
            ActivityCompat.requestPermissions(
                requireActivity(),
                requiredPermission,
                ConstantsTime.PERMISSIONS_REQUEST_CODE
            )
    }

    private fun checkPermission() = ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun checkLocationServicesStatus() =
        with(requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager) {
            isProviderEnabled(LocationManager.GPS_PROVIDER) || isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        }

    @SuppressLint("MissingPermission")
    override fun onStart() {
        super.onStart()
        if (checkPermission())
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    override fun onStop() {
        super.onStop()
        mFusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onMarkerClick(marker: Marker) = false

    companion object {
        private const val UP_RECYCLERVIEW_TIME = 500L
    }

}