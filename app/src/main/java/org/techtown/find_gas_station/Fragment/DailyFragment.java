package org.techtown.find_gas_station.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import org.techtown.find_gas_station.R;


public class DailyFragment extends Fragment {

    private Fragment gasol_Fragment,disel_Fragment,HighGasol_Fragment,kerosene_Fragment,butan_Fragment;
    private TabLayout tabs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gasol_Fragment = new GasolineFragment();
        disel_Fragment = new DieselFragment();
        HighGasol_Fragment = new High_GasolineFragment();
        kerosene_Fragment = new KeroseneFragment();
        butan_Fragment = new ButaneFragment();


        getParentFragmentManager().beginTransaction().add(R.id.frame, gasol_Fragment).commit();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_daily, container, false);

        tabs = rootView.findViewById(R.id.tabs);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                int position = tab.getPosition();

                Fragment selected = null;

                if(position == 0){

                    selected = gasol_Fragment;

                }else if (position == 1){

                    selected = disel_Fragment;

                }else if(position == 2){

                    selected = HighGasol_Fragment;

                }else if(position == 3){

                    selected = kerosene_Fragment;

                }else{

                    selected = butan_Fragment;

                }

                getParentFragmentManager().beginTransaction().replace(R.id.frame, selected).commit();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return rootView;
    }

}