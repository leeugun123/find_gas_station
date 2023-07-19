package org.techtown.find_gas_station.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.techtown.find_gas_station.MVVM.GetOilViewModel;
import org.techtown.find_gas_station.R;


public class GasolineFragment extends Fragment {

    GetOilViewModel getOilViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getOilViewModel = new ViewModelProvider(this).get(GetOilViewModel.class);
        //getOilViewModel 초기화

        getOilViewModel.getOilAvg("20230715","B027");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_gasoline, container, false);



        return rootView;
    }
}