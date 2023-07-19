package org.techtown.find_gas_station.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;

import org.techtown.find_gas_station.MVVM.GetOilViewModel;
import org.techtown.find_gas_station.R;


public class GasolineFragment extends Fragment {

    GetOilViewModel getOilViewModel;

    private LineChart lineChart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getOilViewModel = new ViewModelProvider(this).get(GetOilViewModel.class);
        //getOilViewModel 초기화




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_gasoline, container, false);

        lineChart = rootView.findViewById(R.id.line_chart);
        getOilViewModel.getOilAvg(lineChart,"B027");




        return rootView;
    }
}