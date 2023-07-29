package org.techtown.find_gas_station;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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

import com.google.android.material.snackbar.Snackbar;

import org.techtown.find_gas_station.MVVM.SetViewModel;
import org.techtown.find_gas_station.set.Set;

public class Splash extends AppCompatActivity implements
ActivityCompat.OnRequestPermissionsResultCallback{
    
    private SetViewModel setViewModel;
    private String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};  // 외부 저장소


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        setViewModel = new ViewModelProvider(this).get(SetViewModel.class);

        setViewModel.insert(new Set("B027","1000","1"));
        //null 값 방지


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent intent = new Intent(getApplicationContext(), FragmentActivity.class);
                startActivity(intent);
                finish();


            }
        },500);


    }

    @Override
    protected void onPause() {

        super.onPause();
        finish();
    }





}