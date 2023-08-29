package org.techtown.find_gas_station;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kakao.sdk.common.KakaoSdk;

import org.techtown.find_gas_station.databinding.ActivityIntelBinding;

public class IntelActivity extends AppCompatActivity implements OnMapReadyCallback{

    private ActivityIntelBinding binding;
    private GoogleMap detailMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityIntelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent receive_intent = getIntent();

        String title = receive_intent.getStringExtra("title");
        //binding.title.setText(title);//주유소 이름

        int image = receive_intent.getIntExtra("gas_img",R.drawable.oil_2);
        binding.gasImage.setImageResource(image);
        //주유소 이미지

        String lotAddress = receive_intent.getStringExtra("lotAddress");
        binding.lotAddress.setText(lotAddress);//지번 주소

        String stAddress = receive_intent.getStringExtra("stAddress");
        binding.stAddress.setText(stAddress);//도로명 주소

        String tel = receive_intent.getStringExtra("tel");
        binding.tel.setText(tel);//전화번호

        binding.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
                startActivity(intent);

            }
        });//전화 걸기

        String oil_kind = receive_intent.getStringExtra("oil_kind");

        if(oil_kind.equals("N")){
            binding.oilKind.setText("주유소");
        }
        else if(oil_kind.equals("Y")){
            binding.oilKind.setText("자동차 주유소");
        }
        else{
            binding.oilKind.setText("주유소/충전소 겸업");
        }
        //업종 구분


        String carWash = receive_intent.getStringExtra("carWash");

        if(carWash.equals("Y")){
            binding.carWash.setText("O");
            binding.carWash.setTextColor(Color.parseColor("#009900"));
            //초록색
        }
        else{
            binding.carWash.setText("X");
            binding.carWash.setTextColor(Color.parseColor("#ff0000"));
            //빨간색
        }

        String store = receive_intent.getStringExtra("store");

        if(store.equals("Y")){
            binding.store.setText("O");
            binding.store.setTextColor(Color.parseColor("#009900"));
            //초록색
        }
        else{
            binding.store.setText("X");
            binding.store.setTextColor(Color.parseColor("#ff0000"));
            //빨간색
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.detailMap);
        mapFragment.getMapAsync(this);

        float wgsY = receive_intent.getFloatExtra("wgsY",0);
        float wgsX = receive_intent.getFloatExtra("wgsX",0);

        LatLng pos = new LatLng(wgsY,wgsX);

        MarkerOptions markerOptions = new MarkerOptions();;

        BitmapDrawable bitmapdraw = (BitmapDrawable) binding.gasImage.getResources().getDrawable(image);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b,120,120,false);


        markerOptions.position(pos).title(title).snippet("현 위치로부터 거리" + "2.4km")
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                detailMap.addMarker(markerOptions);

                detailMap.animateCamera(CameraUpdateFactory.newLatLng(
                                new LatLng(wgsY ,wgsX)),
                        600,
                        null
                );

            }
        },500);




    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        Log.e("TAG","세부사항 맵 준비됨");

        detailMap = googleMap;

        detailMap.getUiSettings().setZoomControlsEnabled(true);//확대/축소 컨트롤
        detailMap.getUiSettings().setZoomGesturesEnabled(true);//줌 가능하도록 설정
        detailMap.animateCamera(CameraUpdateFactory.zoomTo(18));//카메라 줌
        detailMap.getUiSettings().setMyLocationButtonEnabled(true);



    }


    public static class GlobalApplication extends Application {

        private static GlobalApplication instance;

        @Override
        public void onCreate(){
            super.onCreate();
            instance = this;
            KakaoSdk.init(this,"{"+ BuildConfig.KAKAO_API_KEY + "}");
        }



    }



}