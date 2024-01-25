package org.techtown.find_gas_station.View.Fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.material.snackbar.Snackbar
import org.techtown.find_gas_station.Adapter.OilInfoAdapter
import org.techtown.find_gas_station.OilCondition
import org.techtown.find_gas_station.OilCondition.afterIntel
import org.techtown.find_gas_station.OilCondition.beforeIntel
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.Util.Constant.ConstantGuide.CHECK_DATA_EMPTY_GUIDE
import org.techtown.find_gas_station.Util.Constant.ConstantGuide.CONFIRM_GUIDE
import org.techtown.find_gas_station.Util.Constant.ConstantGuide.REQUIRE_LOCATION_PERMISSION_GUIDE
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.CHECK_FOUR_SPEND_TIME
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.CHECK_PRICE_CONDITION
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.CHECK_THREE_ROAD_DISTANCE
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.CHECK_TWO_DIRECT_DISTANCE
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.DIRECT_DISTANCE_GUIDE
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.PRICE_CONDITION_GUIDE
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.ROAD_DISTANCE_GUIDE
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.SPEND_TIME_GUIDE
import org.techtown.find_gas_station.Util.Constant.ConstantsTime.FASTEST_UPDATE_INTERVAL_MS
import org.techtown.find_gas_station.Util.Constant.ConstantsTime.PERMISSIONS_REQUEST_CODE
import org.techtown.find_gas_station.Util.Constant.ConstantsTime.UPDATE_INTERVAL_MS
import org.techtown.find_gas_station.Util.Constant.ConstantsTime.UP_RECYCLERVIEW_TIME
import org.techtown.find_gas_station.Util.GPS.GeoTrans
import org.techtown.find_gas_station.Util.GPS.GeoTrans.convert
import org.techtown.find_gas_station.Util.GPS.GeoTransPoint
import org.techtown.find_gas_station.Util.GPS.GpsTracker
import org.techtown.find_gas_station.View.Activity.SettingActivity
import org.techtown.find_gas_station.ViewModel.GetOilListViewModel
import org.techtown.find_gas_station.ViewModel.SetViewModel
import org.techtown.find_gas_station.databinding.FragmentHomeBinding


class HomeFragment : Fragment(), OnMapReadyCallback, OnMarkerClickListener {

    companion object {
        var getWgsMyX = ""
        var getWgsMyY = ""
        const val REQUEST_CODE = 1001
    }

    private val mFusedLocationClient by lazy { LocationServices.getFusedLocationProviderClient(requireActivity()) }
    private val gpsTracker by lazy {GpsTracker(requireActivity())}
    private val mapFragment by lazy { SupportMapFragment.newInstance() }
    private val locationRequest by lazy {
        LocationRequest()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(UPDATE_INTERVAL_MS.toLong())
            .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS.toLong())}

    private val setViewModel by lazy {
        ViewModelProvider(this, SetViewModel.Factory((requireContext().applicationContext as Application)))[SetViewModel::class.java]
    }
    private val getOilListViewModel by lazy { ViewModelProvider(this)[GetOilListViewModel::class.java] }

    private lateinit var mBinding : FragmentHomeBinding
    private lateinit var mMap : GoogleMap
    //private lateinit var currentMarker : Marker
    private val requiredPermission = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    // 앱을 실행하기 위해 필요한 퍼미션을 정의




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun initSetting() {
        windowSetInit()
        locationRequestInit()
        mBinding.listRecycler.layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
    }

    private fun windowSetInit() {
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )
    }//화면이 꺼지지 않도록 유지

    private fun locationRequestInit() {
        LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
    }


    // 비동기로 업데이트된 데이터를 사용하여 UI 업데이트
    private fun updateTextUi() {

        mBinding.arrayFirst.text = when (OilCondition.afterIntel[1]) {
            CHECK_PRICE_CONDITION -> PRICE_CONDITION_GUIDE
            CHECK_TWO_DIRECT_DISTANCE -> DIRECT_DISTANCE_GUIDE
            CHECK_THREE_ROAD_DISTANCE -> ROAD_DISTANCE_GUIDE
            CHECK_FOUR_SPEND_TIME -> SPEND_TIME_GUIDE
            else -> OilCondition.afterIntel[1]
        }

    }

    private fun getOilData() {
        Log.e("TAG" , "HomeFragment _ getOilData")
        progressBarVisible()
        val ge = transFormPoint(gpsTracker.getLatitude().toFloat(), gpsTracker.getLongitude().toFloat())

        /*
        lifecycleScope.launch(Dispatchers.Main){
            withContext(Dispatchers.IO) {
                getOilListViewModel.requestOilList(ge.x.toString(), ge.y.toString(), oilIntel[0], oilIntel[1], oilIntel[2])
            }
        }*/

    }



    private fun progressBarVisible(){
        mBinding.progressBar.visibility = View.VISIBLE
    }

    private fun transFormPoint(latitude : Float, longtitude : Float): GeoTransPoint {
        val point = GeoTransPoint(longtitude.toDouble(), latitude.toDouble())
        wgsInit(point)
        return convert(GeoTrans.GEO, GeoTrans.KATEC, point)
    }

    private fun wgsInit(point : GeoTransPoint) {
        getWgsMyX = point.x.toString()
        getWgsMyY = point.y.toString()
    }

    private fun showEmptyMessage(){
        Toast.makeText(requireContext(), CHECK_DATA_EMPTY_GUIDE, Toast.LENGTH_SHORT).show()
        removeProgressBar()
    }

    private fun removeProgressBar(){
        if (mBinding.progressBar.visibility == View.VISIBLE)
            mBinding.progressBar.visibility = View.GONE
    }


    private fun upRecyclerView() {

        Handler().postDelayed({

            val smoothScroller = object : LinearSmoothScroller(mBinding.listRecycler.context) {
                    override fun getVerticalSnapPreference() = SNAP_TO_START
            }

            smoothScroller.targetPosition = 0
            mBinding.listRecycler.layoutManager!!.startSmoothScroll(smoothScroller)

        }, UP_RECYCLERVIEW_TIME)

    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentHomeBinding.inflate(inflater,container,false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        childFragmentManager.beginTransaction()
            .replace(R.id.map, mapFragment)
            .commit()

        mapFragment.getMapAsync(this)

        initSetting()

        getOilListViewModel.getOilList().observe(viewLifecycleOwner) { list ->

            Log.e("TAG" , "HomeFragment list가 observe 됨")

            removeProgressBar()

            mBinding.listRecycler.adapter = OilInfoAdapter(list, mMap, afterIntel[1])

            upRecyclerView()
            checkListEmpty(list.size)
        }

        setViewModel.getOilLocalData()

        setViewModel.oilLocalData.observe(viewLifecycleOwner) { oilLocalData ->

                oilLocalData?.let {
                    afterIntel[0] = it.oilRad
                    afterIntel[1] = it.oilSort
                    afterIntel[2] = it.oilName
                } ?: run {
                    afterIntel[0] = "1000"
                    afterIntel[1] = "1"
                    afterIntel[2] = "B027"
                }

                syncBeforeAfterIntel()
                requestApi()

        }

        mBinding.reset.setOnClickListener { getOilData() }

        mBinding.setting.setOnClickListener {
            val intent = Intent(activity, SettingActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }

    }

    private fun syncBeforeAfterIntel() {
        beforeIntel[0] = afterIntel[0]
        beforeIntel[1] = afterIntel[1]
        beforeIntel[2] = afterIntel[2]
    }

    private fun checkListEmpty(listSize : Int) {
        if(listSize == 0){
            showEmptyMessage()
        }
    }

    private fun requestApi() {
        showIntelLog()
        getOilData()
        updateTextUi()
    }

    private fun showIntelLog(){
        Log.e("TAG", afterIntel[0])
        Log.e("TAG", afterIntel[1])
        Log.e("TAG", afterIntel[2])
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && checkChangeData()) {
            syncBeforeAfterIntel()
            requestApi()
        }
    }

    private fun checkChangeData() = beforeIntel != afterIntel

    private val locationCallback : LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) { super.onLocationResult(locationResult) }
    }

    //시작 위치 업데이트
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {

        if (checkPermission()) {
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onMapReady(googleMap : GoogleMap) {

        mMap = googleMap

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()), 15f))
        mMap.isMyLocationEnabled = true

        mMap.animateCamera(CameraUpdateFactory.zoomTo(15f))
        mMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isZoomGesturesEnabled = true
            isMyLocationButtonEnabled = true
        }

        if (checkPermission()) {
            startLocationUpdates()
        } else {
            handleLocationPermissionRequest()
        }

        mMap.setOnMapClickListener(OnMapClickListener { })
    }

    private fun handleLocationPermissionRequest() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), requiredPermission[0])) {

            Snackbar.make(mBinding.layoutMain, REQUIRE_LOCATION_PERMISSION_GUIDE, Snackbar.LENGTH_INDEFINITE).setAction(CONFIRM_GUIDE) {
                ActivityCompat.requestPermissions(
                    requireActivity(), requiredPermission,
                    PERMISSIONS_REQUEST_CODE
                )
            }.show()

        } else
            ActivityCompat.requestPermissions(requireActivity(), requiredPermission, PERMISSIONS_REQUEST_CODE)

    }


    // 런타임 퍼미션 처리를 위한 메소드들
    private fun checkPermission() = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED


    //GPS 요청 코드
    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun checkLocationServicesStatus() = with(requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager) {
        isProviderEnabled(LocationManager.GPS_PROVIDER) || isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    override fun onStart() {
        super.onStart()
        Log.e("TAG", "onStart")

        if (checkPermission()) {
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        }

    }

    //백그라운드에서도 화면이 계속 유지
    override fun onStop() {
        super.onStop()
        mFusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onMarkerClick(marker: Marker) = false


}