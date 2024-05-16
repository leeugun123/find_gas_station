package org.techtown.find_gas_station.presentation.ui.oilroundinfo

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
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
import kotlinx.coroutines.flow.collect
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.data.remote.model.station.TotalOilInfo
import org.techtown.find_gas_station.databinding.FragmentStationInfoBinding
import org.techtown.find_gas_station.presentation.common.base.BaseFragment
import org.techtown.find_gas_station.presentation.common.extension.repeatOnStarted
import org.techtown.find_gas_station.presentation.common.util.gps.GeoTrans
import org.techtown.find_gas_station.presentation.common.util.gps.GeoTransPoint
import org.techtown.find_gas_station.presentation.common.util.gps.GpsTracker
import org.techtown.find_gas_station.presentation.ui.oilroundinfo.oilroundrecyclerview.StationInfoAdapter

@AndroidEntryPoint
class StationInfoFragment :
    BaseFragment<FragmentStationInfoBinding>(R.layout.fragment_station_info),
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
            .setInterval(UPDATE_INTERVAL_MS.toLong())
            .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS.toLong())
    }

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {}
    }

    private lateinit var gpsTracker: GpsTracker
    private lateinit var mMap: GoogleMap

    private val requiredPermission = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val stationInfoViewModel: StationInfoViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        childFragmentManager.beginTransaction()
            .replace(R.id.map, mapFragment)
            .commit()

        mapFragment.getMapAsync(this)

        initLocationRequest()
        initBinding()

        observeEmptyCheck()

        checkFlag()
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

    private fun getOilData() {
        if (!stationInfoViewModel.isLoading.value) {
            initGpsTracker()
            val katecPos =
                transFormPoint(
                    gpsTracker.getLatitude().toFloat(),
                    gpsTracker.getLongitude().toFloat()
                )

            stationInfoViewModel.requestOilList(
                wgsX,
                wgsY,
                katecPos.x.toString(),
                katecPos.y.toString()
            )
        }
    }

    private fun initGpsTracker() {
        gpsTracker = GpsTracker(requireContext())
    }

    private fun navigateSettingFragment() {
        requireParentFragment().findNavController()
            .navigate(R.id.action_mainFragment_to_settingFragment)
    }

    private fun observeEmptyCheck() {
        repeatOnStarted {
            stationInfoViewModel.emptyCheck.collect { empty ->
                if (empty)
                    showEmptyMessage()
            }
        }
    }

    private fun showEmptyMessage() {
        Toast.makeText(requireContext(), R.string.data_empty_message, Toast.LENGTH_SHORT).show()
    }

    private fun checkFlag() {
        if (stationInfoViewModel.conditionChangeFlag) {
            requestRoundOilInfo()
            stationInfoViewModel.conditionChangeFlag = false
        }
    }

    private fun requestRoundOilInfo() {
        getOilData()
        updateSortText()
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

    private fun checkPermission() = ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

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

    private fun observeOilList() {
        repeatOnStarted {
            stationInfoViewModel.stationList
                .collect { list ->
                    syncStationUi(list)
                }
        }
    }

    private fun syncStationUi(oilList: List<TotalOilInfo>) {
        binding.listRecycler.adapter =
            StationInfoAdapter(
                oilList,
                mMap,
                stationInfoViewModel.oilCondition.sort,
                totalOilInfoClick = ::navigateToStationDetail
            )
    }

    private fun navigateToStationDetail(stationInfo: TotalOilInfo) {
        val bundle = bundleOf("stationInfo" to stationInfo)

        requireParentFragment().findNavController()
            .navigate(R.id.action_mainFragment_to_stationDetailFragment, bundle)
    }

    private fun handleLocationPermissionRequest() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                requiredPermission[0]
            )
        ) {
            Snackbar.make(
                binding.layoutMain,
                requireContext().getString(R.string.require_location_permission_guide),
                Snackbar.LENGTH_INDEFINITE
            ).setAction(requireContext().getString(R.string.confirm)) {
                ActivityCompat.requestPermissions(
                    requireActivity(), requiredPermission,
                    PERMISSIONS_REQUEST_CODE
                )
            }.show()
        } else
            ActivityCompat.requestPermissions(
                requireActivity(),
                requiredPermission,
                PERMISSIONS_REQUEST_CODE
            )
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
        private const val UPDATE_INTERVAL_MS = 1000 // 1초
        private const val FASTEST_UPDATE_INTERVAL_MS = 500 // 0.5초
        private const val PERMISSIONS_REQUEST_CODE = 100
    }
}