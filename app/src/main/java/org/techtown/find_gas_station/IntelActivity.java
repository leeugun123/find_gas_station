package org.techtown.find_gas_station;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.techtown.find_gas_station.databinding.ActivityIntelBinding;

public class IntelActivity extends AppCompatActivity {

    private ActivityIntelBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }

}