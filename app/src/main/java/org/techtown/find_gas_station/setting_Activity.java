package org.techtown.find_gas_station;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.techtown.find_gas_station.Fragment.HomeFragment;
import org.techtown.find_gas_station.MVVM.SetViewModel;
import org.techtown.find_gas_station.set.Set;

import java.util.ArrayList;

public class setting_Activity extends AppCompatActivity {

    private Button close;
    private Spinner spinner;//기름 스피너
    private Spinner distance_spinner;//반경 스피너
    private Spinner sort_spinner;//정렬 스피너
    String[] oil_intel_setting = new String[3];

    private SetViewModel setViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        setViewModel = new ViewModelProvider(this).get(SetViewModel.class);
        //viewModel 초기화

        spinner = findViewById(R.id.spinner);
        sort_spinner = findViewById(R.id.sort_spinner);
        distance_spinner = findViewById(R.id.distance_spinner);

        setViewModel.getSetLiveData().observe(this, new Observer<Set>() {
            @Override
            public void onChanged(Set set) {

                if (set != null) {
                    // LiveData가 변경될 때마다 UI 업데이트
                    oil_intel_setting[0] = set.getOil_rad() != null ? set.getOil_rad() : "1000";
                    oil_intel_setting[1] = set.getOil_sort() != null ? set.getOil_sort() : "1";
                    oil_intel_setting[2] = set.getOil_name() != null ? set.getOil_name() : "B027";
                }

                updateUI();

            }
        });


        close = findViewById(R.id.go_back);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragment.setFlag = true;
                finish();
            }
        });//닫기 버튼


        //========================== 1. 기름 종류 설정 ===============
        ArrayList<String> stringCategory = new ArrayList<String>();

        stringCategory.add("휘발유");
        stringCategory.add("경유");
        stringCategory.add("고급 휘발유");
        stringCategory.add("실내 등유");
        stringCategory.add("자동차 부탄");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,R.layout.support_simple_spinner_dropdown_item,stringCategory);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK);

                if(i == 0){
                    oil_intel_setting[2] = "B027";
                }
                else if(i == 1){
                    oil_intel_setting[2] = "D047";
                }
                else if(i == 2){
                    oil_intel_setting[2] = "B034";
                }
                else if(i == 3){
                    oil_intel_setting[2] = "C004";
                }
                else{
                    oil_intel_setting[2] = "K015";
                }

                updateUI();

                setViewModel.delete();
                setViewModel.insert(new Set(oil_intel_setting[2],oil_intel_setting[0],oil_intel_setting[1]));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });





        //================= 2. 반경 설정 =============================
        ArrayList<String> distance_string = new ArrayList<String>();

        distance_string.add("1km");
        distance_string.add("3km");
        distance_string.add("5km");

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(
                this,R.layout.support_simple_spinner_dropdown_item,distance_string);
        adapter1.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        distance_spinner.setAdapter(adapter1);


        distance_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK);

                if(i == 0){
                    oil_intel_setting[0] = "1000";
                }//1km
                else if(i == 1){
                    oil_intel_setting[0] = "3000";
                }//3km
                else{
                    oil_intel_setting[0] = "5000";
                }//5km

                updateUI();

                setViewModel.delete();
                setViewModel.insert(new Set(oil_intel_setting[2],oil_intel_setting[0],oil_intel_setting[1]));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //3. 정렬 기준 설정
        ArrayList<String> sort_string = new ArrayList<String>();

        sort_string.add("가격순");
        sort_string.add("직경 거리순");
        sort_string.add("도로 거리순");
        sort_string.add("소요시간 순");

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                this,R.layout.support_simple_spinner_dropdown_item,sort_string);
        adapter2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sort_spinner.setAdapter(adapter2);

        sort_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK);

                if(i == 0)
                    oil_intel_setting[1] = "1";//가격순
                else if(i == 1)
                    oil_intel_setting[1] = "2";//직경 거리순
                else if(i == 2)
                    oil_intel_setting[1] = "3";//도로 거리순
                else if(i == 3)
                    oil_intel_setting[1] = "4";//소요시간 순

                updateUI();

                setViewModel.delete();
                setViewModel.insert(new Set(oil_intel_setting[2],oil_intel_setting[0],oil_intel_setting[1]));


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                    Log.e("TAG","터치 안함");
            }
        });

    }

    private void updateUI() {

        if(oil_intel_setting[2].equals("B027")){
            spinner.setSelection(0);
        }
        else if(oil_intel_setting[2].equals("D047")){
            spinner.setSelection(1);
        }
        else if(oil_intel_setting[2].equals("B034")){
            spinner.setSelection(2);
        }
        else if(oil_intel_setting[2].equals("C004")){
            spinner.setSelection(3);
        }
        else{
            spinner.setSelection(4);
        }

        //자신이 설정했던 spinner를 가져옴
        if(oil_intel_setting[0].equals("1000")){
            distance_spinner.setSelection(0);
        }
        else if(oil_intel_setting[0].equals("3000")){
            distance_spinner.setSelection(1);
        }
        else{
            distance_spinner.setSelection(2);
        }


        if(oil_intel_setting[1].equals("1"))
            sort_spinner.setSelection(0);
        else if(oil_intel_setting[1].equals("2"))
            sort_spinner.setSelection(1);
        else if(oil_intel_setting[1].equals("3"))
            sort_spinner.setSelection(2);
        else if(oil_intel_setting[1].equals("4"))
            sort_spinner.setSelection(3);


    }

    @Override
    public void onBackPressed() {
        HomeFragment.setFlag = true;
        finish();
    }


}