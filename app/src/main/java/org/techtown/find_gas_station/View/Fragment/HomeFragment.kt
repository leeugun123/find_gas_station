package org.techtown.find_gas_station.View.Fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import com.google.android.gms.location.FusedLocationProviderClient
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
import org.techtown.find_gas_station.Util.GPS.GeoTrans
import org.techtown.find_gas_station.Util.GPS.GeoTrans.convert
import org.techtown.find_gas_station.Util.GPS.GeoTransPoint
import org.techtown.find_gas_station.Util.GPS.GpsTracker
import org.techtown.find_gas_station.ViewModel.SetViewModel
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.Repository.SetRepository
import org.techtown.find_gas_station.View.Activity.SettingActivity
import org.techtown.find_gas_station.ViewModel.GetOilListViewModel
//import org.techtown.find_gas_station.ViewModel.SetViewModelFactory
import org.techtown.find_gas_station.databinding.FragmentHomeBinding
import org.techtown.find_gas_station.set.RoomDB


class HomeFragment : Fragment(), OnMapReadyCallback, OnMarkerClickListener {

    companion object {
        var empty = false
        @JvmField
        var getWgsMyX = ""
        @JvmField
        var getWgsMyY = ""
        @JvmField
        var setFlag = true

        private const val UPDATE_INTERVAL_MS = 1000 // 1초
        private const val FASTEST_UPDATE_INTERVAL_MS = 500 // 0.5초
        private const val PERMISSIONS_REQUEST_CODE = 100
    }

    private lateinit var mBinding : FragmentHomeBinding
    private lateinit var mMap : GoogleMap
    private lateinit var currentMarker : Marker //현재 마커
    private lateinit var Red : Bitmap
    private lateinit var mFusedLocationClient : FusedLocationProviderClient
    private lateinit var locationRequest : LocationRequest
    private lateinit var gpsTracker : GpsTracker

    // 앱을 실행하기 위해 필요한 퍼미션을 정의
    private var REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

    private lateinit var setViewModel : SetViewModel
    private val getOilListViewModel by lazy { ViewModelProvider(this)[GetOilListViewModel::class.java] }

    private var oilIntel = arrayOfNulls<String>(3)
    private var notYet = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initSetting()




    }

    private fun initSetting() {
        windowSetInit() //화면이 꺼지지 않도록 유지
        locationRequestInit() //위치요청 세팅
        Red = BitmapFactory.decodeResource(resources, R.drawable.red_marker)
    }

    private fun windowSetInit() {
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )
    }

    private fun locationRequestInit() {
        locationRequest = LocationRequest()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(UPDATE_INTERVAL_MS.toLong())
            .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS.toLong())
        LocationSettingsRequest.Builder().addLocationRequest(locationRequest!!)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }


    // 비동기로 업데이트된 데이터를 사용하여 UI 업데이트
    private fun updateUI() {

        mBinding.arrayFirst!!.text = when (oilIntel[1]) {
            "1" -> "가격순"
            "2" -> "직경 거리순"
            "3" -> "도로 거리순"
            "4" -> "소요 시간순"
            else -> "Unknown"
        }

    }


    //getData메소드 호출하여 ArrayList 값들 채우기
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun init_reset() {
        Log.e("TAG", "init_reset")
        gpsTracker = GpsTracker(requireActivity())
        getData(gpsTracker!!.getLatitude().toFloat(), gpsTracker!!.getLongitude().toFloat())
    }

    private fun getData(latitude : Float, Longtitude : Float) {
        Log.e("TAG", "getData")

        empty = true
        notYet = true

        mBinding.progressBar!!.visibility = View.VISIBLE
        val point = GeoTransPoint(Longtitude.toDouble(), latitude.toDouble())

        getWgsMyX = point.x.toString()
        getWgsMyY = point.y.toString()


        val ge = convert(GeoTrans.GEO, GeoTrans.KATEC, point)//GEO를 KATEC으로 변환

        getOilListViewModel.requestOilList(ge.x.toString(), ge.y.toString(), oilIntel[0].toString(), oilIntel[1].toString(), oilIntel[2].toString())


        Handler().postDelayed(Runnable {

            if (empty) {
                Toast.makeText(requireContext(), "데이터가 비어있거나 서버가 점검 중입니다.", Toast.LENGTH_SHORT).show()
                mBinding.progressBar!!.visibility = View.GONE
            }

        }, 3000)



    }


    private fun upRecyclerView() {

        Handler().postDelayed(Runnable {

            val smoothScroller: SmoothScroller = object : LinearSmoothScroller(mBinding.listRecycler!!.context) {
                    override fun getVerticalSnapPreference() = SNAP_TO_START
            }

            smoothScroller.targetPosition = 0
            mBinding.listRecycler!!.layoutManager!!.startSmoothScroll(smoothScroller)
        }, 500)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.e("TAG", "HomeFragment onCreateView")

        mBinding = FragmentHomeBinding.inflate(inflater,container,false)

        val mapFragment: SupportMapFragment by lazy { SupportMapFragment.newInstance() }

        childFragmentManager.beginTransaction()
            .replace(R.id.map, mapFragment)
            .commit()

        mapFragment.getMapAsync(this)


        getOilListViewModel.getOilList().observe(viewLifecycleOwner, Observer { list ->

            empty = false
            val myRecyclerAdapter = OilInfoAdapter(list!!, mMap, oilIntel[1].toString())
            mBinding.listRecycler.adapter = myRecyclerAdapter
            myRecyclerAdapter.notifyDataSetChanged()

            mBinding.progressBar.visibility = View.GONE

            upRecyclerView()

        })


        val oilDao = RoomDB.getAppDatabase(requireContext()).setDao()
        val repository = SetRepository(oilDao)
        /*
        setViewModel = ViewModelProvider(owner = this, SetViewModelFactory(repository))
            .get(SetViewModel::class.java)

        setViewModel.oilLocalData.observe(viewLifecycleOwner, Observer { oilLocalData ->

            oilIntel[0] = oilLocalData.getOilRad()
            oilIntel[1] = oilLocalData.getOilSort()
            oilIntel[2] = oilLocalData.getOilName()

        })
        */




        mBinding.listRecycler.layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)

        mBinding.reset.setOnClickListener { init_reset() }

        mBinding.setting.setOnClickListener {
            setFlag = false
            startActivity(Intent(requireActivity(), SettingActivity::class.java))
        }

        return mBinding.root
    }


    private val locationCallback : LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) { super.onLocationResult(locationResult) }
    }

    //시작 위치 업데이트
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {

        if (checkPermission()) {
            mFusedLocationClient!!.requestLocationUpdates(locationRequest!!, locationCallback, Looper.myLooper())
            mMap!!.isMyLocationEnabled = true
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onMapReady(googleMap: GoogleMap) {
        Log.e("TAG", "onMapReady")

        mMap = googleMap
        setStartLocation()

        if (checkPermission()) {
            startLocationUpdates()
        } else {
            handleLocationPermissionRequest()
        }
        configureMapSettings()
        // 현재 오동작을 해서 주석처리
        mMap!!.setOnMapClickListener(OnMapClickListener { })
    }

    private fun configureMapSettings() {

        mMap!!.uiSettings.apply {
            isZoomControlsEnabled = true
            isZoomGesturesEnabled = true
            isMyLocationButtonEnabled = true
        }

        mMap!!.animateCamera(CameraUpdateFactory.zoomTo(15f))

    }

    private fun handleLocationPermissionRequest() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), REQUIRED_PERMISSIONS[0])) {

            Snackbar.make(mBinding.layoutMain!!, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Snackbar.LENGTH_INDEFINITE).setAction("확인") {
                ActivityCompat.requestPermissions(
                    requireActivity(), REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                )
            }.show()

        } else
            ActivityCompat.requestPermissions(requireActivity(), REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE)

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
            mFusedLocationClient!!.requestLocationUpdates(locationRequest!!, locationCallback, null)
            mMap!!.isMyLocationEnabled = true
        }

    }

    //백그라운드에서도 화면이 계속 유지
    override fun onStop() {
        Log.e("TAG", "onStop")
        super.onStop()
        mFusedLocationClient!!.removeLocationUpdates(locationCallback)
    }

    //앱이 시작할때 자기 위치로 이동 시켜주는 메소드
    private fun setStartLocation() {
        Log.e("TAG", "setStartLocation")
        gpsTracker = GpsTracker(requireContext())
        currentMarker!!.remove()
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(gpsTracker!!.getLatitude(), gpsTracker!!.getLongitude()), 15f))
    }

    override fun onMarkerClick(marker: Marker) = false


}