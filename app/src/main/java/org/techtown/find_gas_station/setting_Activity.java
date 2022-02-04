package org.techtown.find_gas_station;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class setting_Activity extends AppCompatActivity {


    public static final int SETTING_REQUEST_CODE_OK = 31;


    private Button close;
    //private Button apply

    private Spinner spinner;
    private Spinner distance_spinner;
    private Spinner sort_spinner;

    String[] oil_intel_setting = new String[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        Intent prior_intent = getIntent();

        oil_intel_setting[0] = prior_intent.getStringExtra("oil_rad");//반경범위
        oil_intel_setting[1] = prior_intent.getStringExtra("oil_sort");//정렬기준
        oil_intel_setting[2] = prior_intent.getStringExtra("oil_name");//기름종류



        close = findViewById(R.id.go_back);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

       ArrayList<String> stringCategory = new ArrayList<String>();

       stringCategory.add("휘발유");
       stringCategory.add("경유");
       stringCategory.add("고급 휘발유");
       stringCategory.add("실내 등유");
       stringCategory.add("자동차 부탄");

       spinner = findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            this,R.layout.support_simple_spinner_dropdown_item,stringCategory);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
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
                                                  apply();

                                              }

                                              @Override
                                              public void onNothingSelected(AdapterView<?> adapterView) {

                                              }
                                          });

        ArrayList<String> distance_string = new ArrayList<String>();

        distance_string.add("1km");
        distance_string.add("3km");
        distance_string.add("5km");

        distance_spinner = findViewById(R.id.distance_spinner);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(
                this,R.layout.support_simple_spinner_dropdown_item,distance_string);
        adapter1.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        distance_spinner.setAdapter(adapter1);

        if(oil_intel_setting[0].equals("1000")){
            distance_spinner.setSelection(0);
        }
        else if(oil_intel_setting[0].equals("3000")){
            distance_spinner.setSelection(1);
        }
        else{
            distance_spinner.setSelection(2);
        }


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

                apply();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ArrayList<String> sort_string = new ArrayList<String>();

        sort_string.add("가격순");
        sort_string.add("거리순");

        sort_spinner = findViewById(R.id.sort_spinner);

        if(oil_intel_setting[1].equals("1")){
            sort_spinner.setSelection(0);
        }
        else
            sort_spinner.setSelection(1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                this,R.layout.support_simple_spinner_dropdown_item,sort_string);
        adapter2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sort_spinner.setAdapter(adapter2);

        if(oil_intel_setting.equals("1")){
            sort_spinner.setSelection(1);
        }
        else
            sort_spinner.setSelection(0);

        sort_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK);

                if(i == 0){
                    oil_intel_setting[1] = "1";//가격순
                }
                else{
                    oil_intel_setting[1] = "2";//거리순
                }
                apply();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void apply(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        //intent 삽입 및 전달
        intent.putExtra("oil_rad_reply", oil_intel_setting[0]);
        intent.putExtra("oil_sort_reply", oil_intel_setting[1]);
        intent.putExtra("oil_name_reply", oil_intel_setting[2]);

        setResult(SETTING_REQUEST_CODE_OK, intent);

    }
}