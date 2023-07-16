package org.techtown.find_gas_station;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class FragmentActivity extends AppCompatActivity {


    private long pressedTime = 0;
    private Fragment fa,fb;
    private FragmentManager fragmentManager;
    BottomNavigationView bottomNavigationView;
    //바텀 네비게이션 뷰


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        bottomNavigationView = findViewById(R.id.bottomNav);

        //처음화면

        fragmentManager = getSupportFragmentManager();

        fa = new HomeFragment();
        fragmentManager.beginTransaction().add(R.id.main_frame, fa).commit();

        //바텀 네비게이션뷰 안의 아이템 설정
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.Home_fragment:

                        if(fa == null){
                            fa = new HomeFragment();
                            fragmentManager.beginTransaction().add(R.id.main_frame,fa).commit();
                        }

                        if(fa != null)
                            fragmentManager.beginTransaction().show(fa).commit();
                        if(fb != null)
                            fragmentManager.beginTransaction().hide(fb).commit();

                        break;

                    case R.id.Daily_fragment:

                        if(fb == null){
                            fb = new DailyFragment();
                            fragmentManager.beginTransaction().add(R.id.main_frame, fb).commit();
                        }

                        if(fa != null)
                            fragmentManager.beginTransaction().hide(fa).commit();
                        if(fb != null)
                            fragmentManager.beginTransaction().show(fb).commit();

                        break;
                }

                return true;
            }
        });




    }



    @Override
    public void onBackPressed(){


        if(System.currentTimeMillis() > pressedTime + 2000){
            pressedTime = System.currentTimeMillis();
            Toast.makeText(this,"한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
        else {

            Toast.makeText(this, "종료 완료", Toast.LENGTH_SHORT).show();
            finish();

        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Add any specific handling for configuration changes here
        // For example, you can adjust the layout or UI elements based on the new configuration.
    }

}