package org.techtown.find_gas_station.presentation.setting

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.util.parser.OilParser.calOilName
import org.techtown.find_gas_station.util.parser.OilParser.calOilSort
import org.techtown.find_gas_station.util.parser.OilParser.calRad
import org.techtown.find_gas_station.databinding.ActivityDrawerBinding

class SettingActivity : AppCompatActivity() {

    private val mBinding by lazy { ActivityDrawerBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        mBinding.goBack.setOnClickListener {
            // findNavController().popBackStack()
        }

        spinnerSet()
        updateUI()

    }

    private fun spinnerSet() {

        setupSpinner(
            mBinding.typeSpinner,
            listOf(
                GASOLINE_KOREAN,
                VIA_KOREAN,
                PREMIUM_GASOLINE_KOREAN,
                INDOOR_KEROSENE_KOREAN,
                CAR_BUTANE_KOREAN
            )
        ) { selectedValue ->
            afterIntel[2] = calOilName(selectedValue)
        }

        setupSpinner(
            mBinding.distanceSpinner,
            listOf(ONE_KM, THREE_KM, FIVE_KM)
        ) { selectedValue ->
            afterIntel[0] = calRad(selectedValue)
        }

        setupSpinner(
            mBinding.sortSpinner,
            listOf(
                PRICE_CONDITION_GUIDE,
                DIRECT_DISTANCE_GUIDE,
                ROAD_DISTANCE_GUIDE,
                SPEND_TIME_GUIDE
            )
        ) { selectedValue ->
            afterIntel[1] = calOilSort(selectedValue)
        }

    }


    private fun updateUI() {

        // 기름 종류 설정
        mBinding.typeSpinner.setSelection(
            when (afterIntel[2]) {
                GASOLINE_GUIDE_ENGLISH -> 0 //휘발유
                VIA_GUIDE_ENGLISH -> 1 //경유
                PREMIUM_GASOLINE_ENGLISH -> 2 //고급 휘발유
                INDOOR_KEROSENE_ENGLISH -> 3 //실내 등유
                else -> 4
            }
        )

        // 거리 설정
        mBinding.distanceSpinner.setSelection(
            when (afterIntel[0]) {
                ONE_KM_IN_METERS -> 0
                THREE_KM_IN_METERS -> 1
                else -> 2
            }
        )

        // 정렬 설정
        mBinding.sortSpinner.setSelection(
            when (afterIntel[1]) {
                CHECK_PRICE_CONDITION -> 0
                CHECK_TWO_DIRECT_DISTANCE -> 1
                CHECK_THREE_ROAD_DISTANCE -> 2
                else -> 3
            }
        )

    }


    private fun setupSpinner(spinner: Spinner, options: List<String>, onSelect: (String) -> Unit) {

        val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, options)
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)

        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                adapterView: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {

                (adapterView.getChildAt(0) as TextView).setTextColor(Color.BLACK)
                onSelect(options.getOrElse(position) { "" })
                //updateUI()

            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }

}