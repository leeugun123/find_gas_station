package org.techtown.find_gas_station.MVVM;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.techtown.find_gas_station.oil_list;

import java.util.List;

public class GetOilViewModel extends AndroidViewModel {

    //레트로핏 테스트
    private GetOilRepository getOilRepository;

    public GetOilViewModel(@NonNull Application application) {
        super(application);
        getOilRepository = new GetOilRepository(application);
    }

    public void getOil(String APIkey,String xPos,String yPos,String radius,String sort,String oilKind){

        getOilRepository.getOil(APIkey,xPos,yPos,radius,sort,oilKind);
    }



}
