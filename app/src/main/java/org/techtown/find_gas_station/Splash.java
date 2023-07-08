package org.techtown.find_gas_station;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import org.techtown.find_gas_station.MVVM.SetViewModel;
import org.techtown.find_gas_station.set.Set;

public class Splash extends AppCompatActivity {
    
    SetViewModel setViewModel;

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


                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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