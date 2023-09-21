package org.techtown.find_gas_station.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import org.techtown.find_gas_station.R;


public class DailyFragment extends Fragment {

    private Fragment gasol_Fragment, disel_Fragment, HighGasol_Fragment, kerosene_Fragment, butan_Fragment;
    private TabLayout tabs;
    private Fragment currentFragment; // 현재 보여지고 있는 프래그먼트를 저장하는 변수

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gasol_Fragment = new GasolineFragment();
        disel_Fragment = new DieselFragment();
        HighGasol_Fragment = new High_GasolineFragment();
        kerosene_Fragment = new KeroseneFragment();
        butan_Fragment = new ButaneFragment();

        // 처음에는 가솔린 프래그먼트를 보여줍니다.
        currentFragment = gasol_Fragment;
        getParentFragmentManager().beginTransaction().add(R.id.frame, currentFragment).commit();

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

                if (position == 0) {
                    selected = gasol_Fragment;
                } else if (position == 1) {
                    selected = disel_Fragment;
                } else if (position == 2) {
                    selected = HighGasol_Fragment;
                } else if (position == 3) {
                    selected = kerosene_Fragment;
                } else {
                    selected = butan_Fragment;
                }

                // 현재 보여지고 있는 프래그먼트와 선택된 프래그먼트가 같으면 아무 작업을 하지 않습니다.
                if (currentFragment == selected) {
                    return;
                }

                // 선택된 프래그먼트가 이전에 생성된 적이 있는지 확인합니다.
                if (!selected.isAdded()) {
                    // 선택된 프래그먼트가 이전에 생성된 적이 없다면 추가합니다.
                    getParentFragmentManager().beginTransaction().add(R.id.frame, selected).hide(currentFragment).commit();
                } else {
                    // 선택된 프래그먼트가 이전에 생성된 적이 있다면 숨겨진 프래그먼트를 보여줍니다.
                    getParentFragmentManager().beginTransaction().show(selected).hide(currentFragment).commit();
                }

                currentFragment = selected; // 현재 프래그먼트를 선택된 프래그먼트로 업데이트합니다.
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
