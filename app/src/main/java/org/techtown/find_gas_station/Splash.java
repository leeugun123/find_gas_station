package org.techtown.find_gas_station;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.techtown.find_gas_station.MVVM.SetViewModel;
import org.techtown.find_gas_station.set.Set;

public class Splash extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{
    
    private SetViewModel setViewModel;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        setViewModel = new ViewModelProvider(this).get(SetViewModel.class);

        setViewModel.insert(new Set("B027","1000","1"));
        //null 값 방지1


        if (checkLocationPermission()) {
            // 위치 권한이 허용된 경우, 다음 화면으로 이동
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    startNextActivity();
                }

            },500);


        } else {
            // 위치 권한 요청
            requestLocationPermission();
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    // 위치 권한이 허용되었는지 확인하는 메소드
    private boolean checkLocationPermission() {

        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    }

    // 위치 권한 요청 메소드
    private void requestLocationPermission() {

        ActivityCompat.requestPermissions(this,

                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                },

                LOCATION_PERMISSION_REQUEST_CODE);
    }

    // 위치 권한 요청 결과 처리
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        startNextActivity();
                    }
                },500);

            } else {
                Toast.makeText(this, "위치 권한이 거부되었습니다. 앱을 종료합니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    // 다음 화면으로 이동하는 메소드
    private void startNextActivity() {

        startActivity(new Intent(this, FragmentActivity.class));
        finish();
    }





}