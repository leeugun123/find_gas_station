package org.techtown.find_gas_station;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import org.techtown.find_gas_station.databinding.ActivityIntelBinding;

public class IntelActivity extends AppCompatActivity {

    private ActivityIntelBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityIntelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent receive_intent = getIntent();

        String lotAddress = receive_intent.getStringExtra("lotAddress");
        binding.lotAddress.setText(lotAddress);

        String stAddress = receive_intent.getStringExtra("stAddress");
        binding.stAddress.setText(stAddress);

        String tel = receive_intent.getStringExtra("tel");
        binding.tel.setText(tel);

        String oil_kind = receive_intent.getStringExtra("oil_kind");
        binding.oilKind.setText(oil_kind);

        String carWash = receive_intent.getStringExtra("carWash");
        binding.carWash.setText(carWash);

        String store = receive_intent.getStringExtra("store");
        binding.store.setText(store);


    }

}