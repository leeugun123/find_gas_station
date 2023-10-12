package org.techtown.find_gas_station.Fragment;

import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.snackbar.Snackbar;

import org.techtown.find_gas_station.GPS.GeoTrans;
import org.techtown.find_gas_station.GPS.GeoTransPoint;
import org.techtown.find_gas_station.GPS.GpsTracker;
import org.techtown.find_gas_station.MVVM.GetOilViewModel;
import org.techtown.find_gas_station.MVVM.SetViewModel;
import org.techtown.find_gas_station.Adapter.MyRecyclerAdapter;
import org.techtown.find_gas_station.OilList;
import org.techtown.find_gas_station.R;
import org.techtown.find_gas_station.set.Set;
import org.techtown.find_gas_station.setting_Activity;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment
        implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener{

    //받아오는 list들
    private RecyclerView mRecyclerView;
    public static List<OilList> moil_list;
    private Button Setting;
    public static boolean empty;

    public static String getWgsMyX = "";
    public static String getWgsMyY = "";

    public static boolean setFlag = true;
    //setting activity가 종료된후 변경을 허용하는 flag

    private GoogleMap mMap;//구글 맵
    private Marker currentMarker = null; //현재 마커
    private TextView array_first;
    private Bitmap Red;
    private ProgressBar progressBar;
    private boolean notYet = false;
    private static final int UPDATE_INTERVAL_MS = 1000;  // 1초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500; // 0.5초
    private static final int PERMISSIONS_REQUEST_CODE = 100;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private GpsTracker gpsTracker;
    private Button reset;
    String[] oil_intel = new String[3];
    private View mLayout;

    SetViewModel setViewModel;
    GetOilViewModel getOilViewModel;

    // 앱을 실행하기 위해 필요한 퍼미션을 정의
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};  // 외부 저장소

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //화면이 꺼지지 않도록 유지


        Log.e("TAG","HomeFragment onCreate");


        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);
        //위치요청

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();

        builder.addLocationRequest(locationRequest);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        moil_list = new ArrayList<>();
        setViewModel = new ViewModelProvider(this).get(SetViewModel.class);
        getOilViewModel = new ViewModelProvider(this).get(GetOilViewModel.class);
        Red = BitmapFactory.decodeResource(getResources(), R.drawable.red_marker);
        //viewModel 초기화 부분


        Handler handler = new Handler();

        handler.postDelayed(() -> {

            //싱글톤 패턴을 사용하지 않고 무조건 강제 실행
            //나중에 문제가 생길 수 있음
            setViewModel.getSetLiveData().observe(this, new Observer<Set>() {
                @Override
                public void onChanged(Set set) {
                    if (set != null && setFlag) {
                        // LiveData가 변경될 때마다 UI 업데이트
                        String oil_rad = set.getOil_rad() != null ? set.getOil_rad() : "1000";
                        String oil_sort = set.getOil_sort() != null ? set.getOil_sort() : "1";
                        String oil_name = set.getOil_name() != null ? set.getOil_name() : "B027";

                        oil_intel[0] = oil_rad;
                        oil_intel[1] = oil_sort;
                        oil_intel[2] = oil_name;


                        Log.e("TAG", "데이터 전달");

                        updateUI();

                        init_reset();
                        upRecyclerView();

                    }
                }
            });


        },200);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(!notYet){
                    init_reset();
                    upRecyclerView();
                    notYet = true;
                }

            }
        },500);
        //onMap이 초기화되지 않아 데이터를 가져오지 못하는 경우, 보험으로 실행

    }

    // 비동기로 업데이트된 데이터를 사용하여 UI 업데이트
    private void updateUI() {
        if (oil_intel[1].equals("1")) {
            array_first.setText("가격순");
        } else if (oil_intel[1].equals("2")) {
            array_first.setText("직경 거리순");
        } else if (oil_intel[1].equals("3")) {
            array_first.setText("도로 거리순");
        } else if (oil_intel[1].equals("4")) {
            array_first.setText("소요 시간순");
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void init_reset(){

        Log.e("TAG","init_reset");

        gpsTracker = new GpsTracker(requireActivity());
        getData((float) gpsTracker.getLatitude(),(float) gpsTracker.getLongitude());

    }//getData메소드 호출하여 ArrayList 값들 채우기


    public void getData(float latitude,float Longtitude){

        //인터넷을 사용하는 것이기 때문에 Thread 사용
        //gpsTransfer 클래스를 이용하여 location 매개변수를 사용해 위도,경도 -> x,y좌표로 초기화
        Log.e("TAG","getData");

        HomeFragment.empty = false;

        moil_list.clear();
        MyRecyclerAdapter myRecyclerAdapter = new MyRecyclerAdapter(moil_list,mMap,"1");
        mRecyclerView.setAdapter(myRecyclerAdapter);
        myRecyclerAdapter.notifyDataSetChanged();
        //어뎁터 및 데이터 초기화
        progressBar.setVisibility(View.VISIBLE);


        GeoTransPoint point = new GeoTransPoint(Longtitude,latitude);

        getWgsMyX = String.valueOf(point.getX());
        getWgsMyY = String.valueOf(point.getY());
        //나의 위치 x,y wgs로 저장

        GeoTransPoint ge = GeoTrans.convert(GeoTrans.GEO,GeoTrans.KATEC,point);
        //GEO를 KATEC으로 변환

        if(mMap != null){

            Log.e("TAG", "요청 중");

            getOilViewModel.getOilList(mRecyclerView, mMap, progressBar ,Double.toString(ge.getX()),Double.toString(ge.getY()),
                    oil_intel[0],oil_intel[1],oil_intel[2]);
            notYet = true;
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                if(HomeFragment.empty){
                    Toast.makeText(requireContext(),"데이터가 비어있거나 서버가 점검 중입니다.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        },500);



    }

    //위치가 조회되지 않을때 발생하는 메소드

    public void upRecyclerView(){


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(mRecyclerView.getContext()) {

                    @Override protected int getVerticalSnapPreference() {
                        return LinearSmoothScroller.SNAP_TO_START;
                    }
                };

                smoothScroller.setTargetPosition(moil_list.size()); //itemPosition - 이동시키고자 하는 Item의 Position
                //마지막 배열 = 사용자 View 첫번째 List
                mRecyclerView.getLayoutManager().startSmoothScroll(smoothScroller);

            }
        },500);
        //핸들러를 사용하여 리사이클러뷰가 완전히 형성된 후 최상단으로 리사이클러뷰 올리기



    }



    final LocationCallback locationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Log.e("TAG","onLocationResult");

        }

    };


    //시작 위치 업데이트
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {

        Log.e("TAG","startLocationUpdates");

        int hasFineLocationPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED || hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED)
            return;

        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

        if(checkPermission())
            mMap.setMyLocationEnabled(true);

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mMap = googleMap;

        Log.e("TAG","onMapReady");

        setStartLocation();

        int hasFineLocationPermission = ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED  ) {
            startLocationUpdates();

        }else {

            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), REQUIRED_PERMISSIONS[0])) {

                Snackbar.make(mLayout, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
                        Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        ActivityCompat.requestPermissions( requireActivity(), REQUIRED_PERMISSIONS,
                                PERMISSIONS_REQUEST_CODE);
                    }

                }).show();

            } else {
                ActivityCompat.requestPermissions( requireActivity(), REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }


        }

        mMap.getUiSettings().setZoomControlsEnabled(true);
        //확대/축소 컨트롤
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        //줌 가능하도록 설정
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        //카메라 줌
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        // 현재 오동작을 해서 주석처리

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {


            }

        });

    }


    // 런타임 퍼미션 처리를 위한 메소드들
    private boolean checkPermission() {

        Log.e("TAG","checkPermission");

        int hasFineLocationPermission = ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED   ) {
            return true;
        }

        return false;

    }


    //GPS 요청 코드


    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkLocationServicesStatus() {

        Log.e("TAG","checkLocationServicesStatus");

        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

    }
    //위치서비스상태 확인


    @SuppressLint("MissingPermission")
    @Override
    public void onStart() {
        super.onStart();

        Log.e("TAG","onStart");


        if (checkPermission()) {
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);

            if (mMap!=null)
                mMap.setMyLocationEnabled(true);
        }



    }
    //백그라운드에서도 화면이 계속 유지

    @Override
    public void onStop() {

        Log.e("TAG","onStop");

        super.onStop();

        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        }

    }//백그라운드에서도 화면이 계속 유지


    public void setStartLocation(){

        Log.e("TAG","setStartLocation");

        gpsTracker = new GpsTracker(requireContext());

        LatLng DEFAULT_LOCATION = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
        //현재 위치 설정
        if (currentMarker != null) currentMarker.remove();

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);

        mMap.moveCamera(cameraUpdate);

    }//앱이 시작할때 자기 위치로 이동 시켜주는 메소드


    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        Log.e("TAG","onMarkerClick");
        return false;

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.e("TAG","HomeFragment onCreateView");


        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.map, mapFragment)
                    .commit();
        }//googleMap UI 추가

        mapFragment.getMapAsync(this);


        mRecyclerView = rootView.findViewById(R.id.list_recycle);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(),RecyclerView.VERTICAL,false));


        progressBar = rootView.findViewById(R.id.progressBar);

        mLayout = rootView.findViewById(R.id.layout_main);

        array_first = rootView.findViewById(R.id.array_first);


        reset = rootView.findViewById(R.id.reset);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                init_reset();
                upRecyclerView();

            }
        });

        Setting = rootView.findViewById(R.id.setting);

        Setting.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {

                setFlag = false;
                Intent intent = new Intent(requireActivity(), setting_Activity.class);
                startActivity(intent);

            }//Setting activity로 전환


        });//메뉴 버튼 생성


        return rootView;

    }


}