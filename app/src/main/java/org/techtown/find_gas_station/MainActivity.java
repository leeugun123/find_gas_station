package org.techtown.find_gas_station;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.find_gas_station.GPS.GeoTrans;
import org.techtown.find_gas_station.GPS.GeoTransPoint;
import org.techtown.find_gas_station.GPS.GpsTracker;
import org.techtown.find_gas_station.MVVM.SetViewModel;
import org.techtown.find_gas_station.Retrofit.MyPojo;
import org.techtown.find_gas_station.Retrofit.OIL;
import org.techtown.find_gas_station.Retrofit.RESULT;
import org.techtown.find_gas_station.Retrofit.RetrofitAPI;
import org.techtown.find_gas_station.set.RoomDB;
import org.techtown.find_gas_station.set.Set;
import org.techtown.find_gas_station.set.setting_Activity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//좌표계 변환 문제 KATEC -> 위도,경도

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,ActivityCompat.OnRequestPermissionsResultCallback,
        MyRecyclerAdapter.UserListRecyclerClickListener,GoogleMap.OnMarkerClickListener{

    //받아오는 list들들

    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter myRecyclerAdapter;
    private ArrayList<oil_list> moil_list;

    private Button Setting;

    ArrayList Uid = new ArrayList<>();// 주유소  uid

    ArrayList NAME = new ArrayList<>();//주유소 상호
    ArrayList x_pos = new ArrayList<>();//주유소 X좌표
    ArrayList y_pos = new ArrayList<>();//주유소 Y좌표
    ArrayList gas_price = new ArrayList<>();//주유소 판매가격
    ArrayList storeMarkers = new ArrayList<>();//주유소 정보 표시
    ArrayList distance = new ArrayList<>(); // 거리

    ArrayList trademark = new ArrayList<>();//트레이드 마크


    private int imageResource;

    private GoogleMap mMap;//구글 맵


    private Marker currentMarker = null; //현재 마커

    TextView array_first;

    Bitmap Red;

    public static final int REQUEST_CODE = 100;

    private String API_KEY = BuildConfig.gas_api_key;

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

    //Room DB 변수 추가

    //레트로핏 테스트
    private Retrofit retrofit;
    private final static String BASE_URL = "http:///www.opinet.co.kr/";
    RetrofitAPI retrofitAPI;
    ArrayList<OIL> oilList = new ArrayList<>();


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

        SetViewModel setViewModel;
        setViewModel = new ViewModelProvider(this).get(SetViewModel.class);
        //viewModel 초기화


        reset = findViewById(R.id.reset);//새로고침 버튼
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                init_reset();

                upRecyclerView();
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

        Setting = findViewById(R.id.setting);
        Setting.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),setting_Activity.class);
                startActivityForResult(intent,REQUEST_CODE);

            }//Setting activity로 전환


        });//메뉴 버튼 생성

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                //싱글톤 패턴을 사용하지 않고 무조건 강제 실행
                //나중에 문제가 생길 수 있음

                setViewModel.insert(new Set("B027","1000","1"));
                //null 값 방지

                Set set = setViewModel.getAllSets();

                oil_intel[0] = set.getOil_rad();
                //반경
                oil_intel[1] = set.getOil_sort();
                //정렬 기준
                oil_intel[2] = set.getOil_name();
                //기름 종류


                init_reset();

                Log.e("TAG","실행이 됩니다.");

                array_first = findViewById(R.id.array_first);

                if(oil_intel[1].equals("1")){
                    array_first.setText("가격순");
                }
                else
                    array_first.setText("거리순");



            }
        },100);

        //Handler를 이용하지 않으면 googleMap 오류가 생기므로 핸들러 처리

        upRecyclerView();



    }


    @SuppressLint("UseCompatLoadingForDrawables")
    public void init_reset(){


        gpsTracker = new GpsTracker(MainActivity.this);
        //gpsTracker 가져오기

        getData((float) gpsTracker.getLatitude(),(float) gpsTracker.getLongitude());
        //getData메소드 호출하여 ArrayList 값들 채우기

        //레트로핏 객체 생성
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitAPI = retrofit.create(RetrofitAPI.class);

        retrofitAPI.getOilList(API_KEY,"json","314681.8","544837","5000","B027","1")
                .enqueue(new Callback<MyPojo>() {

                    @Override
                    public void onResponse(Call<MyPojo> call, Response<MyPojo> response) {

                        if(response.isSuccessful()){

                            MyPojo myPojo = response.body();
                            RESULT result = myPojo.getRESULT();
                            OIL oil = result.getOIL()[0];

                            Log.d("TAG",oil.toString());




                        }

                    }

                    @Override
                    public void onFailure(Call<MyPojo> call, Throwable t) {

                    }

                });











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

            GeoTransPoint point = new GeoTransPoint((float)x_pos.get(i), (float)y_pos.get(i));

            GeoTransPoint out = GeoTrans.convert(GeoTrans.KATEC, GeoTrans.GEO,point);
            //KATEC -> Wgs84좌표계로 변경

            moil_list.add(new oil_list((String)Uid.get(i),(String) NAME.get(i),Integer.toString((int)gas_price.get(i)),((int)Math.round((Double)distance.get(i)))+"m",
                    ok,imageResource, (float)out.getX(),(float)out.getY()));

            //moil_list 수정



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
            markerOptions.snippet("현 위치에서부터의 거리 " + (double) distance.get(i) +"m");
            markerOptions.draggable(true);

            BitmapDrawable bitmapdraw;

            if(trademark.get(i).equals("SKE")){
                bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.sk);
            }
            else if(trademark.get(i).equals("GSC")){
                bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.gs);
            }
            else if(trademark.get(i).equals("HDO")){
                bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.hdoil);
            }
            else if(trademark.get(i).equals("SOL")){
                bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.so);
            }
            else if(trademark.get(i).equals("RTO")){
                bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.rto);
            }//비슷
            else if(trademark.get(i).equals("RTX")){
                bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.rto);
            }//비슷
            else if(trademark.get(i).equals("NHO")){
                bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.nho);
            }
            else if(trademark.get(i).equals("E1G")){
                bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.e1);
            }
            else if(trademark.get(i).equals("SKG")){
                bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.skgas);

            }
            else
                bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.oil_2);



            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b,60,60,false);
            //Marker 이미지 표시
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            storeMarkers.add(mMap.addMarker(markerOptions));//stroe Marker를 지도위에 다시 띄운다.

        }

        upRecyclerView();
        //스크롤 뷰 최상단으로 올리기

    }







    //문자열 파싱
    public void JsonParse(String str){

        try {
            JSONObject obj = new JSONObject(str);
            JSONObject ar = (JSONObject) obj.get("RESULT");
            JSONArray arr = (JSONArray) ar.get("OIL");

            for(int i=0; i<arr.length(); i++){

                JSONObject dataObj = arr.getJSONObject(i);
                Uid.add(dataObj.getString("UNI_ID"));
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



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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

        }

    }




    public void getData(float latitude,float Longtitude){

        x_pos.clear();
        y_pos.clear();
        //위치정보 초기화
        Uid.clear();//id 초기화
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
                    //GEO를 KATEC으로 변환

                    URL url = new URL("http://www.opinet.co.kr/api/aroundAll.do?code="+ API_KEY +"&x="+ge.getX()+"&y="+ ge.getY() +"&radius="+ oil_intel[0] +"&sort="+ oil_intel[1] +"&prodcd="+ oil_intel[2] +"&out=json");

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


    public void upRecyclerView(){

        //Log.d("TAG", "리사이클러뷰 위로 올리기!");//locationList의 size는 0 이상이다.

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(mRecyclerView.getContext()) {

                    @Override protected int getVerticalSnapPreference() {
                        return LinearSmoothScroller.SNAP_TO_START;
                    }
                };

                smoothScroller.setTargetPosition(NAME.size()); //itemPosition - 이동시키고자 하는 Item의 Position
                //마지막 배열 = 사용자 View 첫번째 List
                mRecyclerView.getLayoutManager().startSmoothScroll(smoothScroller);

            }
        },100);
        //핸들러를 사용하여
        // 리사이클러뷰가 완전히 형성된 후 최상단으로 리사이클러뷰 올리기

    }



    final LocationCallback locationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);

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

        Log.d( TAG, "지도가 준비되었습니다.!");

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


    //여기 코드는 보지 않아 된다. GPS 요청 코드이다.
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

            //초기 어플을 시작할때  실행되는 메소드

            if (check_result) {

                // 퍼미션을 허용했다면 위치 업데이트를 시작합니다.

                setStartLocation();//주변 위치로 지도 업데이트
                startLocationUpdates();//자기 위치 설정
                init_reset();//주변 정보 가져오기

                Log.d( TAG, "위치를 업데이트 합니다.");
                //초기 어플을 설치할때 나타남

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


    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }//위치서비스상태 확인

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


    public void setStartLocation(){


        gpsTracker = new GpsTracker(MainActivity.this);

        LatLng DEFAULT_LOCATION = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
        //현재 위치 설정
        if (currentMarker != null) currentMarker.remove();

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);

        mMap.moveCamera(cameraUpdate);

        Log.d( TAG, "초기설정");

    }//앱이 시작할때 자기 위치로 이동 시켜주는 메소드




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

    //recycerView를 클릭하면 그 위치로 view가 이동함


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








