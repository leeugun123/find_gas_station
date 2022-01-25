package org.techtown.find_gas_station;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.CarrierConfigManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.api.LogDescriptor;
import com.google.firebase.firestore.GeoPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.find_gas_station.MyRecyclerAdapter.UserListRecyclerClickListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

//좌표계 변환 문제 KATEC -> 위도,경도

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,ActivityCompat.OnRequestPermissionsResultCallback,
        MyRecyclerAdapter.UserListRecyclerClickListener,GoogleMap.OnMarkerClickListener{

    //받아오는 list들들

    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter myRecyclerAdapter;
    private ArrayList<oil_list> moil_list;

    private Button Setting;

    ArrayList NAME = new ArrayList<>();//주유소 상호
    ArrayList x_pos = new ArrayList<>();//주유소 X좌표
    ArrayList y_pos = new ArrayList<>();//주유소 Y좌표
    ArrayList gas_price = new ArrayList<>();//주유소 판매가격
    ArrayList storeMarkers = new ArrayList<>();//주유소 정보 표시
    ArrayList distance = new ArrayList<>(); // 거리

    ArrayList trademark = new ArrayList<>();//트레이드 마크

    private int imageResource;

    private GoogleMap mMap;//구글 맵

    public static final int SETTING_REQUEST_CODE = 30;
    public static final int SETTING_REQUEST_CODE_OK = 31;


    private Marker currentMarker = null; //현재 마커

    TextView array_first;

    Bitmap Red;

    //private String API_KEY = "F211129251";
    private static final String TAG = "googlemap_example";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 1000;  // 1초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500; // 0.5초

    // onRequestPermissionsResult에서 수신된 결과에서 ActivityCompat.requestPermissions를 사용한 퍼미션 요청을 구별하기 위해 사용됩니다.
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    boolean needRequest = false;


    // 앱을 실행하기 위해 필요한 퍼미션을 정의합니다.
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};  // 외부 저장소

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private GpsTracker gpsTracker;

    private Button reset;

    String[] oil_intel = new String[3];

    private View mLayout;  // Snackbar 사용하기 위해서는 View가 필요합니다.
    // (참고로 Toast에서는 Context가 필요했습니다.)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//화면이 꺼지지 않도록 유지

        setContentView(R.layout.activity_main);

        moil_list = new ArrayList<>();

        mRecyclerView = (RecyclerView) findViewById(R.id.list_recycle);
        myRecyclerAdapter = new MyRecyclerAdapter(moil_list,this);

        mRecyclerView.setAdapter(myRecyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,true));


        mLayout = findViewById(R.id.layout_main);



        reset = findViewById(R.id.reset);//새로고침 버튼
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                init_reset();
            }
        });

        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);
        //위치요청

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();

        builder.addLocationRequest(locationRequest);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Red = BitmapFactory.decodeResource(getResources(),R.drawable.red_marker);

        oil_intel[0] = "1000";
        oil_intel[1] = "1";
        oil_intel[2] = "B027";

        Setting = findViewById(R.id.setting);
        Setting.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),setting_Activity.class);
                intent.putExtra("oil_rad",oil_intel[0]);
                intent.putExtra("oil_sort",oil_intel[1]);
                intent.putExtra("oil_name",oil_intel[2]);
                startActivityForResult(intent,SETTING_REQUEST_CODE);
            }//프래그먼트 전환s

        });//메뉴 버튼 생성

        array_first = findViewById(R.id.array_first);
        if(oil_intel[1].equals("1")){
            array_first.setText("가격순");
        }
        else
            array_first.setText("거리순");

        setup_reset();



    }


    public void setup_reset(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                init_reset();
            }
        },100);
    }

    public void init_reset(){

        Log.d("TAG", "locationList가 0이상입니다.");//locationList의 size는 0 이상이다.
        gpsTracker = new GpsTracker(MainActivity.this);

        getData((float) gpsTracker.getLatitude(),(float) gpsTracker.getLongitude());//getData메소드 호출

        //list view에 출력

        mRecyclerView = (RecyclerView) findViewById(R.id.list_recycle);
        myRecyclerAdapter = new MyRecyclerAdapter(moil_list,this);

        mRecyclerView.setAdapter(myRecyclerAdapter);
        moil_list = new ArrayList<>();
        moil_list.clear();

        String ok= "";
        if(oil_intel[2].equals("B027")){
            ok = "휘발유";
        }
        else if(oil_intel[2].equals("D047")){
            ok = "경유";
        }
        else if(oil_intel[2].equals("B034")){
            ok = "고급휘발유";
        }
        else if(oil_intel[2].equals("C004")){
            ok = "실내등유";
        }
        else
            ok = "자동차부탄";

        //주유소 상표 마크 표시
        for(int i=x_pos.size()-1; i>-1; i--){

            if(trademark.get(i).equals("SKE")){
                imageResource = R.drawable.sk;
            }
            else if(trademark.get(i).equals("GSC")){
                imageResource = R.drawable.gs;
            }
            else if(trademark.get(i).equals("HDO")){
                imageResource = R.drawable.hdoil;
            }
            else if(trademark.get(i).equals("SOL")){
                imageResource = R.drawable.so;
            }
            else if(trademark.get(i).equals("RTO")){
                imageResource = R.drawable.rto;
            }//비슷
            else if(trademark.get(i).equals("RTX")){
                imageResource = R.drawable.rto;
            }//비슷
            else if(trademark.get(i).equals("RTX")){
                imageResource = R.drawable.rto;
            }
            else if(trademark.get(i).equals("NHO")){
                imageResource = R.drawable.nho;
            }
            else if(trademark.get(i).equals("E1G")){
                imageResource = R.drawable.e1;
            }
            else if(trademark.get(i).equals("SKG")){
                imageResource = R.drawable.skgas;
            }
            else
                imageResource = R.drawable.oil_2;

            moil_list.add(new oil_list((String) NAME.get(i),Integer.toString((int)gas_price.get(i)),Double.toString((double)distance.get(i)),ok,imageResource));
        }

        myRecyclerAdapter.setOil_lists(moil_list);
        storeMarkers.clear();//storemarker 제거
        mMap.clear();

        //이쪽 코드에는 아무런 문제가 없다. 정확한 위치가 표시됨.

        for (int i = 0; i < x_pos.size(); i++) {

            GeoTransPoint point = new GeoTransPoint((float)x_pos.get(i), (float)y_pos.get(i));
            //ypos가 위도
            GeoTransPoint out = GeoTrans.convert(GeoTrans.KATEC, GeoTrans.GEO,point);

            LatLng temp = new LatLng(out.getY(),out.getX());//좌표변환

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(temp); //지정하는 포지션 표시
            markerOptions.title((String) NAME.get(i)+" 가격 "+ (int) gas_price.get(i) + "원");//주유소 명
            markerOptions.snippet("현 위치에서부터의 거리 " + (double) distance.get(i) +"M");
            markerOptions.draggable(true);

            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.oil_2);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b,90,90,false);
            //Marker 이미지 표시
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            storeMarkers.add(mMap.addMarker(markerOptions));//stroe Marker를 지도위에 다시 띄운다.
        }

    }



    @Override
    public void onMapReady(final GoogleMap googleMap) {
        Log.d(TAG, "onMapReady :");

        mMap = googleMap;
        //런타임 퍼미션 요청 대화상자나 GPS 활성 요청 대화상자 보이기전에
        //지도의 초기위치를 서울로 이동

        setStartLocation();


        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED   ) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)

            startLocationUpdates(); // 3. 위치 업데이트 시작


        }else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Snackbar.make(mLayout, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
                        Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                        ActivityCompat.requestPermissions( MainActivity.this, REQUIRED_PERMISSIONS,
                                PERMISSIONS_REQUEST_CODE);
                    }
                }).show();


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions( this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

        mMap.getUiSettings().setZoomControlsEnabled(true);//확대/축소 컨트롤
        mMap.getUiSettings().setZoomGesturesEnabled(true);//줌 가능하도록 설정
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));//카메라 줌
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        // 현재 오동작을 해서 주석처리

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                Log.d( TAG, "onMapClick :");
            }
        });
    }

    final LocationCallback locationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            //init_reset();
            //계속해서 업데이트 된다....
            //계속 콜백함수로 호출되는 함수
        }

    };



    //시작 위치 업데이트
    private void startLocationUpdates() {

        if (!checkLocationServicesStatus()) {
            Log.d(TAG, "startLocationUpdates : call showDialogForLocationServiceSetting");
            showDialogForLocationServiceSetting();
        }else {

            int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);

            if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED ||
                    hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED   ) {
                Log.d(TAG, "startLocationUpdates : 퍼미션 안가지고 있음");
                return;
            }

            Log.d(TAG, "startLocationUpdates : call mFusedLocationClient.requestLocationUpdates");

            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

            if (checkPermission())
                mMap.setMyLocationEnabled(true);

        }

    }


    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }//위치서비스상태 확인


    //문자열 파싱
    public void JsonParse(String str){

        try {
            JSONObject obj = new JSONObject(str);
            JSONObject ar = (JSONObject) obj.get("RESULT");
            JSONArray arr = (JSONArray) ar.get("OIL");

            for(int i=0; i<arr.length(); i++){
                JSONObject dataObj = arr.getJSONObject(i);
                distance.add(dataObj.getDouble("DISTANCE"));
                NAME.add(dataObj.getString("OS_NM"));//상호명
                gas_price.add(dataObj.getInt("PRICE"));//가격
                x_pos.add((float)dataObj.getDouble("GIS_X_COOR"));
                y_pos.add((float)dataObj.getDouble("GIS_Y_COOR"));
                trademark.add(dataObj.getString("POLL_DIV_CD"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void setStartLocation(){
        gpsTracker = new GpsTracker(MainActivity.this);

        LatLng DEFAULT_LOCATION = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
        //현재 위치 설정
        if (currentMarker != null) currentMarker.remove();

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 10);

        mMap.moveCamera(cameraUpdate);
    }//앱이 시작할때 자기 위치로 이동 시켜주는 메소드

    public void getData(float latitude,float Longtitude){
        x_pos.clear();
        y_pos.clear();
        //위치정보 초기화
        NAME.clear();//주유소 이름 초기화
        gas_price.clear();
        distance.clear();//거리 초기화
        //미리 Arraylist를 초기화 하는 것이 좋다.

        //인터넷을 사용하는 것이기 때문에 Thread 사용
        //gpsTransfer 클래스를 이용하여 location 매개변수를 사용해 위도,경도 -> x,y좌표로 초기화
        Thread readData = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    GeoTransPoint point = new GeoTransPoint(Longtitude,latitude);
                    GeoTransPoint ge = GeoTrans.convert(GeoTrans.GEO,GeoTrans.KATEC,point);

                    URL url = new URL("http://www.opinet.co.kr/api/aroundAll.do?code=F211129251&x="+ge.getX()+"&y="+ ge.getY() +"&radius="+ oil_intel[0] +"&sort="+ oil_intel[1] +"&prodcd="+ oil_intel[2] +"&out=json");

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");//get 가져오기
                    connection.setDoInput(true);

                    InputStream is = connection.getInputStream();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));

                    String result;
                    while((result = br.readLine()) != null) {
                        sb.append(result + "\n");
                    }//일단 가져오는 것은 문제가 없음

                    result = sb.toString();
                    Log.d("result",result);
                    JsonParse(result);
                }catch (Exception e){}
            }
        });
        readData.start();

        try {
            readData.join();
        }catch (Exception e){}


    }

    //위치가 조회되지 않을때 발생하는 메소드

    //여기부터는 런타임 퍼미션 처리을 위한 메소드들
    private boolean checkPermission() {

        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED   ) {
            return true;
        }

        return false;

    }



    //여기 코드는 보지 않아도 된다. GPS 요청 코드이다.
    /*
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {
        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults);
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;

            // 모든 퍼미션을 허용했는지 체크합니다.
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if (check_result) {

                // 퍼미션을 허용했다면 위치 업데이트를 시작합니다.
                startLocationUpdates();
            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {


                    // 사용자가 거부만 선택한 경우에는 앱을 다시 실행하여 허용을 선택하면 앱을 사용할 수 있습니다.
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            finish();
                        }
                    }).show();

                } else {


                    // "다시 묻지 않음"을 사용자가 체크하고 거부를 선택한 경우에는 설정(앱 정보)에서 퍼미션을 허용해야 앱을 사용할 수 있습니다.
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            finish();
                        }
                    }).show();
                }
            }

        }
    }





    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG, "onStart");

        if (checkPermission()) {

            Log.d(TAG, "onStart : call mFusedLocationClient.requestLocationUpdates");
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);

            if (mMap!=null)
                mMap.setMyLocationEnabled(true);

        }


    }//백그라운드에서도 화면이 계속 유지될수 있도록 함.

    @Override
    protected void onStop() {

        super.onStop();

        if (mFusedLocationClient != null) {

            Log.d(TAG, "onStop : call stopLocationUpdates");
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }//백그라운드에서도 화면이 계속 유지될수 있도록 함.


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SETTING_REQUEST_CODE){
            if(resultCode == SETTING_REQUEST_CODE_OK){

                oil_intel[0] = data.getStringExtra("oil_rad_reply");
                oil_intel[1] = data.getStringExtra("oil_sort_reply");
                oil_intel[2] = data.getStringExtra("oil_name_reply");

                if(oil_intel[1].equals("1")){
                    array_first.setText("가격순");
                }
                else
                    array_first.setText("거리순");

                init_reset();
            }
        }

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d(TAG, "onActivityResult : GPS 활성화 되있음");
                        needRequest = true;
                        return;
                    }
                }
                break;
        }
    }

    @Override
    public void onUserClicked(int position) {
        Log.d(TAG,"onUserClicked: selected a user: " + moil_list.get(position).get_oil_name());

        String O_name = moil_list.get(position).get_oil_name();

        for(int i=0; i<NAME.size(); i++){
            if(O_name.equals(NAME.get(i))){

                GeoTransPoint point = new GeoTransPoint((float)x_pos.get(i), (float)y_pos.get(i));
                GeoTransPoint out = GeoTrans.convert(GeoTrans.KATEC, GeoTrans.GEO,point);
                //좌표변환

                mMap.animateCamera(CameraUpdateFactory.newLatLng(
                        new LatLng(out.getY(),out.getX())),
                        600,
                        null
                );


                mMap.setOnMarkerClickListener(this);


                break;
            }
        }
    }


    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }
}
/**
 * @author aquilegia
 *
 * The code based on hyosang(http://hyosang.kr/tc/96) and aero's blog ((http://aero.sarang.net/map/analysis.html)
 *	License:  LGPL : http://www.gnu.org/copyleft/lesser.html
 */


class GeoTrans {

    public static final int GEO=0;
    public static final int KATEC=1;
    public static final int TM=2;
    public static final int GRS80=3;

    private static double[] m_Ind = new double[3];
    private static double[] m_Es = new double[3];
    private static double[] m_Esp = new double[3];
    private static double[] src_m = new double[3];
    private static double[] dst_m = new double[3];

    private static double EPSLN = 0.0000000001;
    private static double[] m_arMajor = new double[3];
    private static double[] m_arMinor = new double[3];

    private static double[] m_arScaleFactor = new double[3];
    private static double[] m_arLonCenter = new double[3];
    private static double[] m_arLatCenter = new double[3];
    private static double[] m_arFalseNorthing = new double[3];
    private static double[] m_arFalseEasting = new double[3];

    private static double[] datum_params = new double[3];

    static {
        m_arScaleFactor[GEO] = 1;
        m_arLonCenter[GEO] = 0.0;
        m_arLatCenter[GEO] = 0.0;
        m_arFalseNorthing[GEO] = 0.0;
        m_arFalseEasting[GEO] = 0.0;
        m_arMajor[GEO] = 6378137.0;
        m_arMinor[GEO] = 6356752.3142;

        m_arScaleFactor[KATEC] = 0.9999;//0.9996;
        //m_arLonCenter[KATEC] = 2.22529479629277; // 127.5
        m_arLonCenter[KATEC] = 2.23402144255274; // 128
        m_arLatCenter[KATEC] = 0.663225115757845;
        m_arFalseNorthing[KATEC] = 600000.0;
        m_arFalseEasting[KATEC] = 400000.0;
        m_arMajor[KATEC] = 6377397.155;
        m_arMinor[KATEC] = 6356078.9633422494;

        m_arScaleFactor[TM] = 1.0;
        //this.m_arLonCenter[TM] = 2.21656815003280; // 127
        m_arLonCenter[TM] = 2.21661859489671; // 127.+10.485 minute
        m_arLatCenter[TM] = 0.663225115757845;
        m_arFalseNorthing[TM] = 500000.0;
        m_arFalseEasting[TM] = 200000.0;
        m_arMajor[TM] = 6377397.155;
        m_arMinor[TM] = 6356078.9633422494;

        datum_params[0] = -146.43;
        datum_params[1] = 507.89;
        datum_params[2] = 681.46;

        double tmp = m_arMinor[GEO] / m_arMajor[GEO];
        m_Es[GEO] = 1.0 - tmp * tmp;
        m_Esp[GEO] = m_Es[GEO] / (1.0 - m_Es[GEO]);

        if (m_Es[GEO] < 0.00001) {
            m_Ind[GEO] = 1.0;
        } else {
            m_Ind[GEO] = 0.0;
        }

        tmp = m_arMinor[KATEC] / m_arMajor[KATEC];
        m_Es[KATEC] = 1.0 - tmp * tmp;
        m_Esp[KATEC] = m_Es[KATEC] / (1.0 - m_Es[KATEC]);

        if (m_Es[KATEC] < 0.00001) {
            m_Ind[KATEC] = 1.0;
        } else {
            m_Ind[KATEC] = 0.0;
        }

        tmp = m_arMinor[TM] / m_arMajor[TM];
        m_Es[TM] = 1.0 - tmp * tmp;
        m_Esp[TM] = m_Es[TM] / (1.0 - m_Es[TM]);

        if (m_Es[TM] < 0.00001) {
            m_Ind[TM] = 1.0;
        } else {
            m_Ind[TM] = 0.0;
        }

        src_m[GEO] = m_arMajor[GEO] * mlfn(e0fn(m_Es[GEO]), e1fn(m_Es[GEO]), e2fn(m_Es[GEO]), e3fn(m_Es[GEO]), m_arLatCenter[GEO]);
        dst_m[GEO] = m_arMajor[GEO] * mlfn(e0fn(m_Es[GEO]), e1fn(m_Es[GEO]), e2fn(m_Es[GEO]), e3fn(m_Es[GEO]), m_arLatCenter[GEO]);
        src_m[KATEC] = m_arMajor[KATEC] * mlfn(e0fn(m_Es[KATEC]), e1fn(m_Es[KATEC]), e2fn(m_Es[KATEC]), e3fn(m_Es[KATEC]), m_arLatCenter[KATEC]);
        dst_m[KATEC] = m_arMajor[KATEC] * mlfn(e0fn(m_Es[KATEC]), e1fn(m_Es[KATEC]), e2fn(m_Es[KATEC]), e3fn(m_Es[KATEC]), m_arLatCenter[KATEC]);
        src_m[TM] = m_arMajor[TM] * mlfn(e0fn(m_Es[TM]), e1fn(m_Es[TM]), e2fn(m_Es[TM]), e3fn(m_Es[TM]), m_arLatCenter[TM]);
        dst_m[TM] = m_arMajor[TM] * mlfn(e0fn(m_Es[TM]), e1fn(m_Es[TM]), e2fn(m_Es[TM]), e3fn(m_Es[TM]), m_arLatCenter[TM]);
    }

    private static double D2R(double degree) {
        return degree* Math.PI / 180.0;
    }

    private static double R2D(double radian) {
        return radian * 180.0 / Math.PI;
    }

    private static double e0fn(double x) {
        return 1.0 - 0.25 * x * (1.0 + x / 16.0 * (3.0 + 1.25 * x));
    }

    private static double e1fn(double x) {
        return 0.375 * x * (1.0 + 0.25 * x * (1.0 + 0.46875 * x));
    }

    private static double e2fn(double x) {
        return 0.05859375 * x * x * (1.0 + 0.75 * x);
    }

    private static double e3fn(double x) {
        return x * x * x * (35.0 / 3072.0);
    }

    private static double mlfn(double e0, double e1, double e2, double e3, double phi) {
        return e0 * phi - e1 * Math.sin(2.0 * phi) + e2 * Math.sin(4.0 * phi) - e3 * Math.sin(6.0 * phi);
    }

    private static double asinz(double value) {
        if (Math.abs(value) > 1.0) value = (value > 0 ? 1: -1);
        return Math.asin(value);
    }

    public static GeoTransPoint convert(int srctype, int dsttype, GeoTransPoint in_pt) {
        GeoTransPoint tmpPt = new GeoTransPoint();
        GeoTransPoint out_pt = new GeoTransPoint();

        if (srctype == GEO) {
            tmpPt.x = D2R(in_pt.x);
            tmpPt.y = D2R(in_pt.y);
        } else {
            tm2geo(srctype, in_pt, tmpPt);
        }

        if (dsttype == GEO) {
            out_pt.x = R2D(tmpPt.x);
            out_pt.y = R2D(tmpPt.y);
        } else {
            geo2tm(dsttype, tmpPt, out_pt);
            //out_pt.x = Math.round(out_pt.x);
            //out_pt.y = Math.round(out_pt.y);
        }

        return out_pt;
    }

    public static void geo2tm(int dsttype, GeoTransPoint in_pt, GeoTransPoint out_pt) {
        double x, y;

        transform(GEO, dsttype, in_pt);
        double delta_lon = in_pt.x - m_arLonCenter[dsttype];
        double sin_phi = Math.sin(in_pt.y);
        double cos_phi = Math.cos(in_pt.y);

        if (m_Ind[dsttype] != 0) {
            double b = cos_phi * Math.sin(delta_lon);

            if ((Math.abs(Math.abs(b) - 1.0)) < EPSLN) {
                //Log.d("무한대 에러");
                //System.out.println("무한대 에러");
            }
        } else {
            double b = 0;
            x = 0.5 * m_arMajor[dsttype] * m_arScaleFactor[dsttype] * Math.log((1.0 + b) / (1.0 - b));
            double con = Math.acos(cos_phi * Math.cos(delta_lon) / Math.sqrt(1.0 - b * b));

            if (in_pt.y < 0) {
                con = con * -1;
                y = m_arMajor[dsttype] * m_arScaleFactor[dsttype] * (con - m_arLatCenter[dsttype]);
            }
        }

        double al = cos_phi * delta_lon;
        double als = al * al;
        double c = m_Esp[dsttype] * cos_phi * cos_phi;
        double tq = Math.tan(in_pt.y);
        double t = tq * tq;
        double con = 1.0 - m_Es[dsttype] * sin_phi * sin_phi;
        double n = m_arMajor[dsttype] / Math.sqrt(con);
        double ml = m_arMajor[dsttype] * mlfn(e0fn(m_Es[dsttype]), e1fn(m_Es[dsttype]), e2fn(m_Es[dsttype]), e3fn(m_Es[dsttype]), in_pt.y);

        out_pt.x = m_arScaleFactor[dsttype] * n * al * (1.0 + als / 6.0 * (1.0 - t + c + als / 20.0 * (5.0 - 18.0 * t + t * t + 72.0 * c - 58.0 * m_Esp[dsttype]))) + m_arFalseEasting[dsttype];
        out_pt.y = m_arScaleFactor[dsttype] * (ml - dst_m[dsttype] + n * tq * (als * (0.5 + als / 24.0 * (5.0 - t + 9.0 * c + 4.0 * c * c + als / 30.0 * (61.0 - 58.0 * t + t * t + 600.0 * c - 330.0 * m_Esp[dsttype]))))) + m_arFalseNorthing[dsttype];
    }


    public static void tm2geo(int srctype, GeoTransPoint in_pt, GeoTransPoint out_pt) {
        GeoTransPoint tmpPt = new GeoTransPoint(in_pt.getX(), in_pt.getY());
        int max_iter = 6;

        if (m_Ind[srctype] != 0) {
            double f = Math.exp(in_pt.x / (m_arMajor[srctype] * m_arScaleFactor[srctype]));
            double g = 0.5 * (f - 1.0 / f);
            double temp = m_arLatCenter[srctype] + tmpPt.y / (m_arMajor[srctype] * m_arScaleFactor[srctype]);
            double h = Math.cos(temp);
            double con = Math.sqrt((1.0 - h * h) / (1.0 + g * g));
            out_pt.y = asinz(con);

            if (temp < 0) out_pt.y *= -1;

            if ((g == 0) && (h == 0)) {
                out_pt.x = m_arLonCenter[srctype];
            } else {
                out_pt.x = Math.atan(g / h) + m_arLonCenter[srctype];
            }
        }

        tmpPt.x -= m_arFalseEasting[srctype];
        tmpPt.y -= m_arFalseNorthing[srctype];

        double con = (src_m[srctype] + tmpPt.y / m_arScaleFactor[srctype]) / m_arMajor[srctype];
        double phi = con;

        int i = 0;

        while (true) {
            double delta_Phi = ((con + e1fn(m_Es[srctype]) * Math.sin(2.0 * phi) - e2fn(m_Es[srctype]) * Math.sin(4.0 * phi) + e3fn(m_Es[srctype]) * Math.sin(6.0 * phi)) / e0fn(m_Es[srctype])) - phi;
            phi = phi + delta_Phi;

            if (Math.abs(delta_Phi) <= EPSLN) break;

            if (i >= max_iter) {
                //Log.d("무한대 에러");
                //System.out.println("무한대 에러");
                break;
            }

            i++;
        }

        if (Math.abs(phi) < (Math.PI / 2)) {
            double sin_phi = Math.sin(phi);
            double cos_phi = Math.cos(phi);
            double tan_phi = Math.tan(phi);
            double c = m_Esp[srctype] * cos_phi * cos_phi;
            double cs = c * c;
            double t = tan_phi * tan_phi;
            double ts = t * t;
            double cont = 1.0 - m_Es[srctype] * sin_phi * sin_phi;
            double n = m_arMajor[srctype] / Math.sqrt(cont);
            double r = n * (1.0 - m_Es[srctype]) / cont;
            double d = tmpPt.x / (n * m_arScaleFactor[srctype]);
            double ds = d * d;
            out_pt.y = phi - (n * tan_phi * ds / r) * (0.5 - ds / 24.0 * (5.0 + 3.0 * t + 10.0 * c - 4.0 * cs - 9.0 * m_Esp[srctype] - ds / 30.0 * (61.0 + 90.0 * t + 298.0 * c + 45.0 * ts - 252.0 * m_Esp[srctype] - 3.0 * cs)));
            out_pt.x = m_arLonCenter[srctype] + (d * (1.0 - ds / 6.0 * (1.0 + 2.0 * t + c - ds / 20.0 * (5.0 - 2.0 * c + 28.0 * t - 3.0 * cs + 8.0 * m_Esp[srctype] + 24.0 * ts))) / cos_phi);
        } else {
            out_pt.y = Math.PI * 0.5 * Math.sin(tmpPt.y);
            out_pt.x = m_arLonCenter[srctype];
        }
        transform(srctype, GEO, out_pt);
    }

    public static double getDistancebyGeo(GeoTransPoint pt1, GeoTransPoint pt2) {
        double lat1 = D2R(pt1.y);
        double lon1 = D2R(pt1.x);
        double lat2 = D2R(pt2.y);
        double lon2 = D2R(pt2.x);

        double longitude = lon2 - lon1;
        double latitude = lat2 - lat1;

        double a = Math.pow(Math.sin(latitude / 2.0), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(longitude / 2.0), 2);
        return 6376.5 * 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));
    }

    public static double getDistancebyKatec(GeoTransPoint pt1, GeoTransPoint pt2) {
        pt1 = convert(KATEC, GEO, pt1);
        pt2 = convert(KATEC, GEO, pt2);

        return getDistancebyGeo(pt1, pt2);
    }

    public static double getDistancebyTm(GeoTransPoint pt1, GeoTransPoint pt2) {
        pt1 = convert(TM, GEO, pt1);
        pt2 = convert(TM, GEO, pt2);

        return getDistancebyGeo(pt1, pt2);
    }

    private static long getTimebySec(double distance) {
        return Math.round(3600 * distance / 4);
    }

    public static long getTimebyMin(double distance) {
        return (long)(Math.ceil(getTimebySec(distance) / 60));
    }

	/*
	Author:       Richard Greenwood rich@greenwoodmap.com
	License:      LGPL as per: http://www.gnu.org/copyleft/lesser.html
	*/

    /**
     * convert between geodetic coordinates (longitude, latitude, height)
     * and gecentric coordinates (X, Y, Z)
     * ported from Proj 4.9.9 geocent.c
     */

    // following constants from geocent.c
    private static final double HALF_PI = 0.5 * Math.PI;
    private static final double COS_67P5  = 0.38268343236508977;  /* cosine of 67.5 degrees */
    private static final double AD_C      = 1.0026000 ;
    /* Toms region 1 constant */

    private static void transform(int srctype, int dsttype, GeoTransPoint point) {
        if (srctype == dsttype)
            return;

        if (srctype != 0 || dsttype != 0) {
            // Convert to geocentric coordinates.
            geodetic_to_geocentric(srctype, point);

            // Convert between datums
            if (srctype != 0) {
                geocentric_to_wgs84(point);
            }

            if (dsttype != 0) {
                geocentric_from_wgs84(point);
            }

            // Convert back to geodetic coordinates
            geocentric_to_geodetic(dsttype, point);
        }
    }

    private static boolean geodetic_to_geocentric (int type, GeoTransPoint p) {

        /*
         * The function Convert_Geodetic_To_Geocentric converts geodetic coordinates
         * (latitude, longitude, and height) to geocentric coordinates (X, Y, Z),
         * according to the current ellipsoid parameters.
         *
         *    Latitude  : Geodetic latitude in radians                     (input)
         *    Longitude : Geodetic longitude in radians                    (input)
         *    Height    : Geodetic height, in meters                       (input)
         *    X         : Calculated Geocentric X coordinate, in meters    (output)
         *    Y         : Calculated Geocentric Y coordinate, in meters    (output)
         *    Z         : Calculated Geocentric Z coordinate, in meters    (output)
         *
         */

        double Longitude = p.x;
        double Latitude = p.y;
        double Height = p.z;
        double X;  // output
        double Y;
        double Z;

        double Rn;            /*  Earth radius at location  */
        double Sin_Lat;       /*  Math.sin(Latitude)  */
        double Sin2_Lat;      /*  Square of Math.sin(Latitude)  */
        double Cos_Lat;       /*  Math.cos(Latitude)  */

        /*
         ** Don't blow up if Latitude is just a little out of the value
         ** range as it may just be a rounding issue.  Also removed longitude
         ** test, it should be wrapped by Math.cos() and Math.sin().  NFW for PROJ.4, Sep/2001.
         */
        if (Latitude < -HALF_PI && Latitude > -1.001 * HALF_PI )
            Latitude = -HALF_PI ;
        else if (Latitude > HALF_PI && Latitude < 1.001 * HALF_PI )
            Latitude = HALF_PI;
        else if ((Latitude < -HALF_PI) || (Latitude > HALF_PI)) { /* Latitude out of range */
            return true;
        }

        /* no errors */
        if (Longitude > Math.PI)
            Longitude -= (2*Math.PI);
        Sin_Lat = Math.sin(Latitude);
        Cos_Lat = Math.cos(Latitude);
        Sin2_Lat = Sin_Lat * Sin_Lat;
        Rn = m_arMajor[type] / (Math.sqrt(1.0e0 - m_Es[type] * Sin2_Lat));
        X = (Rn + Height) * Cos_Lat * Math.cos(Longitude);
        Y = (Rn + Height) * Cos_Lat * Math.sin(Longitude);
        Z = ((Rn * (1 - m_Es[type])) + Height) * Sin_Lat;

        p.x = X;
        p.y = Y;
        p.z = Z;
        return false;
    } // cs_geodetic_to_geocentric()


    /** Convert_Geocentric_To_Geodetic
     * The method used here is derived from 'An Improved Algorithm for
     * Geocentric to Geodetic Coordinate Conversion', by Ralph Toms, Feb 1996
     */
    private static void geocentric_to_geodetic (int type, GeoTransPoint p) {

        double X = p.x;
        double Y = p.y;
        double Z = p.z;
        double Longitude;
        double Latitude = 0.;
        double Height;

        double W;        /* distance from Z axis */
        double W2;       /* square of distance from Z axis */
        double T0;       /* initial estimate of vertical component */
        double T1;       /* corrected estimate of vertical component */
        double S0;       /* initial estimate of horizontal component */
        double S1;       /* corrected estimate of horizontal component */
        double Sin_B0;   /* Math.sin(B0), B0 is estimate of Bowring aux doubleiable */
        double Sin3_B0;  /* cube of Math.sin(B0) */
        double Cos_B0;   /* Math.cos(B0) */
        double Sin_p1;   /* Math.sin(phi1), phi1 is estimated latitude */
        double Cos_p1;   /* Math.cos(phi1) */
        double Rn;       /* Earth radius at location */
        double Sum;      /* numerator of Math.cos(phi1) */
        boolean At_Pole;  /* indicates location is in polar region */

        At_Pole = false;
        if (X != 0.0) {
            Longitude = Math.atan2(Y,X);
        }
        else {
            if (Y > 0) {
                Longitude = HALF_PI;
            }
            else if (Y < 0) {
                Longitude = -HALF_PI;
            }
            else {
                At_Pole = true;
                Longitude = 0.0;
                if (Z > 0.0) {  /* north pole */
                    Latitude = HALF_PI;
                }
                else if (Z < 0.0) {  /* south pole */
                    Latitude = -HALF_PI;
                }
                else {  /* center of earth */
                    Latitude = HALF_PI;
                    Height = -m_arMinor[type];
                    return;
                }
            }
        }
        W2 = X*X + Y*Y;
        W = Math.sqrt(W2);
        T0 = Z * AD_C;
        S0 = Math.sqrt(T0 * T0 + W2);
        Sin_B0 = T0 / S0;
        Cos_B0 = W / S0;
        Sin3_B0 = Sin_B0 * Sin_B0 * Sin_B0;
        T1 = Z + m_arMinor[type] * m_Esp[type] * Sin3_B0;
        Sum = W - m_arMajor[type] * m_Es[type] * Cos_B0 * Cos_B0 * Cos_B0;
        S1 = Math.sqrt(T1*T1 + Sum * Sum);
        Sin_p1 = T1 / S1;
        Cos_p1 = Sum / S1;
        Rn = m_arMajor[type] / Math.sqrt(1.0 - m_Es[type] * Sin_p1 * Sin_p1);
        if (Cos_p1 >= COS_67P5) {
            Height = W / Cos_p1 - Rn;
        }
        else if (Cos_p1 <= -COS_67P5) {
            Height = W / -Cos_p1 - Rn;
        }
        else {
            Height = Z / Sin_p1 + Rn * (m_Es[type] - 1.0);
        }
        if (At_Pole == false) {
            Latitude = Math.atan(Sin_p1 / Cos_p1);
        }

        p.x = Longitude;
        p.y =Latitude;
        p.z = Height;
        return;
    } // geocentric_to_geodetic()



    /****************************************************************/
    // geocentic_to_wgs84(defn, p )
    //  defn = coordinate system definition,
    //  p = point to transform in geocentric coordinates (x,y,z)
    private static void geocentric_to_wgs84(GeoTransPoint p) {

        //if( defn.datum_type == PJD_3PARAM )
        {
            // if( x[io] == HUGE_VAL )
            //    continue;
            p.x += datum_params[0];
            p.y += datum_params[1];
            p.z += datum_params[2];
        }
    } // geocentric_to_wgs84

    /****************************************************************/
    // geocentic_from_wgs84()
    //  coordinate system definition,
    //  point to transform in geocentric coordinates (x,y,z)
    private static void geocentric_from_wgs84(GeoTransPoint p) {

        //if( defn.datum_type == PJD_3PARAM )
        {
            //if( x[io] == HUGE_VAL )
            //    continue;
            p.x -= datum_params[0];
            p.y -= datum_params[1];
            p.z -= datum_params[2];

        }
    } //geocentric_from_wgs84()

}


class GeoTransPoint {

    double x;

    double y;

    double z;


    /**

     *

     */

    public GeoTransPoint() {

        super();

    }


    /**

     * @param x

     * @param y

     */

    public GeoTransPoint(double x, double y) {

        super();

        this.x = x;

        this.y = y;

        this.z = 0;

    }


    /**

     * @param x

     * @param y

     * @param y

     */

    public GeoTransPoint(double x, double y, double z) {

        super();

        this.x = x;

        this.y = y;

        this.z = 0;

    }


    public double getX() {

        return x;

    }



    public double getY() {

        return y;

    }

}


