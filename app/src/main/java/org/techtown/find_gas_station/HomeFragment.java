package org.techtown.find_gas_station;

import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import org.techtown.find_gas_station.GPS.GeoTrans;
import org.techtown.find_gas_station.GPS.GeoTransPoint;
import org.techtown.find_gas_station.GPS.GpsTracker;
import org.techtown.find_gas_station.MVVM.GetOilViewModel;
import org.techtown.find_gas_station.MVVM.SetViewModel;
import org.techtown.find_gas_station.set.Set;
import org.techtown.find_gas_station.set.setting_Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class HomeFragment extends Fragment
        implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnMarkerClickListener{

    //받아오는 list들
    private RecyclerView mRecyclerView;
    public static List<oil_list> moil_list;
    private Button Setting;
    public static boolean complete,empty;
    private GoogleMap mMap;//구글 맵
    private Marker currentMarker = null; //현재 마커
    private TextView array_first;
    private Bitmap Red;

    public static final int REQUEST_CODE = 100;
    private static final String TAG = "googlemap_example";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 1000;  // 1초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500; // 0.5초
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    boolean needRequest = false;
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

        Log.e("TAG","onCreate");

        requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //화면이 꺼지지 않도록 유지

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
        //setViewModel 초기화
        getOilViewModel = new ViewModelProvider(this).get(GetOilViewModel.class);
        //getOilViewModel 초기화

        Red = BitmapFactory.decodeResource(getResources(),R.drawable.red_marker);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                //싱글톤 패턴을 사용하지 않고 무조건 강제 실행
                //나중에 문제가 생길 수 있음
                setViewModel.insert(new Set("B027","1000","1"));
                //null 값 방지

                Set set = setViewModel.getAllSets();

                if(set.getOil_rad() == null)
                    oil_intel[0] = "1000";
                else
                    oil_intel[0] = set.getOil_rad();
                //반경

                if(set.getOil_sort() == null){
                    oil_intel[1] = "1";
                }
                else
                    oil_intel[1] = set.getOil_sort();
                //정렬 기준

                if(set.getOil_name() == null){
                    oil_intel[2] = "B027";
                }
                else
                    oil_intel[2] = set.getOil_name();
                //기름 종류

                init_reset();


                if(oil_intel[1].equals("1")){
                    array_first.setText("가격순");
                }
                else
                    array_first.setText("거리순");


            }
        },200);

        //Handler를 이용하지 않으면 googleMap 오류가 생기므로 핸들러 처리

        upRecyclerView();


    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void init_reset(){

        gpsTracker = new GpsTracker(requireActivity());
        getData((float) gpsTracker.getLatitude(),(float) gpsTracker.getLongitude());

    }//getData메소드 호출하여 ArrayList 값들 채우기


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                oil_intel[0] = data.getStringExtra("0");
                //반경
                oil_intel[1] = data.getStringExtra("1");
                //정렬 기준
                oil_intel[2] = data.getStringExtra("2");
                //기름 종류

                if(oil_intel[1].equals("1")){
                    array_first.setText("가격순");
                }
                else
                    array_first.setText("거리순");


                init_reset();

                switch (requestCode) {

                    case GPS_ENABLE_REQUEST_CODE:

                        //사용자가 GPS 활성 시켰는지 검사
                        if (checkLocationServicesStatus() && checkLocationServicesStatus()) {
                            needRequest = true;
                            return;
                        }

                        break;

                }


            }

        }

    }


    public void getData(float latitude,float Longtitude){


        //인터넷을 사용하는 것이기 때문에 Thread 사용
        //gpsTransfer 클래스를 이용하여 location 매개변수를 사용해 위도,경도 -> x,y좌표로 초기화

        HomeFragment.complete = false;
        HomeFragment.empty = false;


        HomeFragment.CheckTypesTask task = new HomeFragment.CheckTypesTask();
        task.execute();
        //프로그래스바를 쓰던 안쓰던 데이터를 가져오는 속도는 똑같음.


        GeoTransPoint point = new GeoTransPoint(Longtitude,latitude);
        GeoTransPoint ge = GeoTrans.convert(GeoTrans.GEO,GeoTrans.KATEC,point);
        //GEO를 KATEC으로 변환


        getOilViewModel.getOilList(mRecyclerView, mMap, Double.toString(ge.getX()),Double.toString(ge.getY()),oil_intel[0],oil_intel[1],oil_intel[2]);


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
        }

    };


    //시작 위치 업데이트
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {

        if (!checkLocationServicesStatus()) {

            Log.d(TAG, "startLocationUpdates : call showDialogForLocationServiceSetting");
            showDialogForLocationServiceSetting();

        }else {

            int hasFineLocationPermission = ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION);
            int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION);

            if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED ||
                    hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED  ) {
                return;
            }

            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

            if (checkPermission())
                mMap.setMyLocationEnabled(true);

        }

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mMap = googleMap;
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

    /*
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {
        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults);
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            boolean check_result = true;

            // 모든 퍼미션을 허용 체크
            for (int result : grandResults) {

                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }

            }

            if (check_result) {

                setStartLocation();
                //주변 위치로 지도 업데이트
                startLocationUpdates();
                //자기 위치 설정
                init_reset();
                //주변 정보 가져오기

            } else {

                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), REQUIRED_PERMISSIONS[1])) {

                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            requireActivity().finish();
                        }

                    }).show();

                } else {

                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            requireActivity().finish();
                        }

                    }).show();

                }

            }



        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkLocationServicesStatus() {

        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

    }
    //위치서비스상태 확인

    //GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);

        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        builder.create().show();

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onStart() {
        super.onStart();

        if (checkPermission()) {
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);

            if (mMap!=null)
                mMap.setMyLocationEnabled(true);
        }


    }
    //백그라운드에서도 화면이 계속 유지

    @Override
    public void onStop() {

        super.onStop();

        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        }

    }//백그라운드에서도 화면이 계속 유지


    public void setStartLocation(){

        gpsTracker = new GpsTracker(requireContext());

        LatLng DEFAULT_LOCATION = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
        //현재 위치 설정
        if (currentMarker != null) currentMarker.remove();

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);

        mMap.moveCamera(cameraUpdate);

    }//앱이 시작할때 자기 위치로 이동 시켜주는 메소드


    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }


    //대기 프로그래스바 클래스
    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog asyncDialog = new ProgressDialog(requireContext());

        @Override
        protected void onPreExecute() {

            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("데이터를 가져오는 중..");
            asyncDialog.show();

            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            asyncDialog.dismiss();
            super.onPostExecute(result);

        }

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.map, mapFragment)
                    .commit();
        }

        mapFragment.getMapAsync(this);


        mRecyclerView = rootView.findViewById(R.id.list_recycle);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(),RecyclerView.VERTICAL,false));


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

                Intent intent = new Intent(requireActivity(), setting_Activity.class);
                startActivityForResult(intent,REQUEST_CODE);

            }//Setting activity로 전환


        });//메뉴 버튼 생성


        return rootView;

    }


}