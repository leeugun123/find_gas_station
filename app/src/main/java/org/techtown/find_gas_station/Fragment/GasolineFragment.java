package org.techtown.find_gas_station.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;

import org.techtown.find_gas_station.MVVM.GetOilViewModel;
import org.techtown.find_gas_station.R;


public class GasolineFragment extends Fragment {

    private GetOilViewModel getOilViewModel;
    private LineChart lineChart;
    private RecyclerView oilAvg_recyclerView;
    private TextView priceText;


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

        oilAvg_recyclerView = rootView.findViewById(R.id.oilAvg_recyclerView);
        oilAvg_recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        priceText = rootView.findViewById(R.id.priceText);
        lineChart = rootView.findViewById(R.id.line_chart);
        getOilViewModel.getOilAvg(lineChart,oilAvg_recyclerView,priceText,"B027");





        return rootView;
    }
}