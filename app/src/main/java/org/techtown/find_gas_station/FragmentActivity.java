package org.techtown.find_gas_station;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FragmentActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView; //바텀 네비게이션 뷰

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        bottomNavigationView = findViewById(R.id.bottomNav);

        //처음화면
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, new HomeFragment()).commit();

        //바텀 네비게이션뷰 안의 아이템 설정
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.Home_fragment:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new HomeFragment()).commit();
                        break;
                    case R.id.Daily_fragment:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new DailyFragment()).commit();
                        break;
                }
                return true;
            }
        });


    }
}