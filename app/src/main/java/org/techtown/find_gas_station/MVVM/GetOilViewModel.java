package org.techtown.find_gas_station.MVVM;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.GoogleMap;

import org.techtown.find_gas_station.MyRecyclerAdapter;
import org.techtown.find_gas_station.oil_list;

import java.util.List;

public class GetOilViewModel extends AndroidViewModel {

    //레트로핏 테스트
    private GetOilRepository getOilRepository;

    public GetOilViewModel(@NonNull Application application) {
        super(application);
        getOilRepository = new GetOilRepository(application);
    }

    //현재 위치 중심으로 주유소 리스트 가져오기
    public void getOilList(RecyclerView mRecyclerView, GoogleMap mMap,
                       String xPos, String yPos, String radius, String sort, String oilKind){

        getOilRepository.getOilList(mRecyclerView, mMap,xPos,yPos,radius,sort,oilKind);
    }



}
