package org.techtown.find_gas_station.presentation.ui.oilroundinfo

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSmoothScroller
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
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.data.TotalOilInfo
import org.techtown.find_gas_station.databinding.FragmentStationInfoBinding
import org.techtown.find_gas_station.presentation.ui.oilroundinfo.oilroundrecyclerview.StationInfoAdapter
import org.techtown.find_gas_station.util.constant.ConstantGuide
import org.techtown.find_gas_station.util.constant.ConstantsTime
import org.techtown.find_gas_station.util.gps.GeoTrans
import org.techtown.find_gas_station.util.gps.GeoTransPoint
import org.techtown.find_gas_station.util.gps.GpsTracker

@AndroidEntryPoint
class StationInfoFragment : Fragment(),
    OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var binding: FragmentStationInfoBinding
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
        override fun onLocationResult(locationResult: LocationResult) {}
    }

    private lateinit var smoothScroller: LinearSmoothScroller
    private lateinit var gpsTracker: GpsTracker
    private lateinit var mMap: GoogleMap

    private val requiredPermission = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val stationInfoViewModel: StationInfoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_station_info, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        childFragmentManager.beginTransaction()
            .replace(R.id.map, mapFragment)
            .commit()

        mapFragment.getMapAsync(this)

        initSetting()
        initBinding()
        initSmoothScroller()

        requestRoundOilInfo()
    }

    private fun initSetting() {
        initWindowSet()
        initLocationRequest()
    }

    private fun initWindowSet() {
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )
    }

    private fun initLocationRequest() {
        LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
    }

    private fun initBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.requestOilDataClick = ::getOilData
        binding.moveToSettingFragmentClick = ::navigateSettingFragment
        binding.stationInfoViewModel = stationInfoViewModel
    }

    private fun navigateSettingFragment() {
        requireParentFragment().findNavController()
            .navigate(R.id.action_mainFragment_to_settingFragment)
    }

    private fun initSmoothScroller() {
        smoothScroller = object : LinearSmoothScroller(binding.listRecycler.context) {
            override fun getVerticalSnapPreference() = SNAP_TO_START
        }
    }

    private fun observeOilList() {
        lifecycleScope.launch(Dispatchers.Main) {
            stationInfoViewModel.oilListFlow.collect {list ->
                repeatOnLifecycle(Lifecycle.State.STARTED){
                    Log.e("TAG","collect  " + list.size)
                    oilListUiSync(list)
                }
            }
        }
    }

    private fun oilListUiSync(oilList: List<TotalOilInfo>) {

        binding.listRecycler.adapter =
            StationInfoAdapter(
                oilList,
                mMap,
                stationInfoViewModel.oilCondition.sort,
                totalOilInfoClick = ::navigateToStationDetail
            )

        checkListEmpty(oilList.size)

        if (stationInfoViewModel.conditionChangeFlag)
            upRecyclerView()
    }

    private fun checkListEmpty(oilListSize: Int) {
        if (oilListSize == 0)
            showEmptyMessage()
    }

    private fun upRecyclerView() {
        lifecycleScope.launch(Dispatchers.Main) {
            delay(UP_RECYCLERVIEW_TIME)
            smoothScroller.targetPosition = 0
            binding.listRecycler.layoutManager!!.startSmoothScroll(smoothScroller)
        }
    }

    private fun requestRoundOilInfo() {
        if (stationInfoViewModel.conditionChangeFlag) {
            getOilData()
            updateSortText()
        }
    }

    private fun getOilData() {
        if(!stationInfoViewModel.isLoading.value){
            initGpsTracker()
            val katecPos =
                transFormPoint(gpsTracker.getLatitude().toFloat(), gpsTracker.getLongitude().toFloat())

            stationInfoViewModel.requestOilList(
                wgsX,
                wgsY,
                katecPos.x.toString(),
                katecPos.y.toString(),
            )
        }
    }

    private fun initGpsTracker() {
        gpsTracker = GpsTracker(requireContext())
    }

    private fun updateSortText() {
        stationInfoViewModel.sortText = when (stationInfoViewModel.oilCondition.sort) {
            "1" -> requireContext().getString(R.string.sort_price)
            "2" -> requireContext().getString(R.string.sort_direct_distance)
            "3" -> requireContext().getString(R.string.sort_road_distance)
            "4" -> requireContext().getString(R.string.sort_spend_time)
            else -> ""
        }
    }

    private fun navigateToStationDetail(stationInfo: TotalOilInfo) {
        keepConditionChangeFlag()
        val bundle = bundleOf("stationInfo" to stationInfo)

        requireParentFragment().findNavController()
            .navigate(R.id.action_mainFragment_to_stationDetailFragment, bundle)
    }

    private fun keepConditionChangeFlag() {
        stationInfoViewModel.conditionChangeFlag = false
    }

    private fun showEmptyMessage() {
        Toast.makeText(requireContext(), R.string.data_empty_message, Toast.LENGTH_SHORT).show()
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

        observeOilList()
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