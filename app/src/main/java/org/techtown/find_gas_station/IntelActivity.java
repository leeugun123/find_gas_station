package org.techtown.find_gas_station;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import org.techtown.find_gas_station.databinding.ActivityIntelBinding;

public class IntelActivity extends AppCompatActivity {

    private ActivityIntelBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityIntelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent receive_intent = getIntent();

        String title = receive_intent.getStringExtra("title");
        binding.title.setText(title);//주유소 이름

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
            binding.carWash.setTextColor(Color.parseColor("#009900"));
            //초록색
        }
        else{
            binding.store.setText("X");
            binding.store.setTextColor(Color.parseColor("#ff0000"));
            //빨간색
        }


    }

}