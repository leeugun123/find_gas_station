package org.techtown.find_gas_station.MVVM;

import android.app.Application;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.google.android.gms.maps.GoogleMap;

import org.techtown.find_gas_station.OilList;

import java.util.ArrayList;

public class GetOilViewModel extends AndroidViewModel {

    //레트로핏 테스트
    private GetOilRepository getOilRepository;

    private MutableLiveData<ArrayList<OilList>> oilLiveList;

    public GetOilViewModel(@NonNull Application application) {
        super(application);
        getOilRepository = new GetOilRepository(application);
        oilLiveList = getOilRepository.getOilList();
    }

    public void insertOilList(String xPos, String yPos, String radius, String sort, String oilKind){
        getOilRepository.insert(xPos, yPos, radius, sort,oilKind);
    }

    //현재 위치 중심으로 주유소 리스트 가져오기
    public MutableLiveData<ArrayList<OilList>> getSetOilList(){
        return oilLiveList;
    }



    public void getOilAvg(LineChart lineChart, RecyclerView oilAvg_recyclerView, TextView priceText, String prodcd){

        getOilRepository.getOilAvg(lineChart,oilAvg_recyclerView,priceText,prodcd);
    }



}
