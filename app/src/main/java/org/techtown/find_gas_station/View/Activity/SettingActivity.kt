package org.techtown.find_gas_station.View.Activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.techtown.find_gas_station.Data.set.OilData
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.CAR_BUTANE
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.CHECK_PRICE_CONDITION
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.CHECK_THREE_ROAD_DISTANCE
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.CHECK_TWO_DIRECT_DISTANCE
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.DIRECT_DISTANCE_GUIDE
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.FIVE_KM
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.GASOLINE_GUIDE
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.GASOLINE_GUIDE_ENGLISH
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.INDOOR_KEROSENE
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.INDOOR_KEROSENE_ENGLISH
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.ONE_KM
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.ONE_KM_IN_METERS
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.PREMIUM_GASOLINE
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.PREMIUM_GASOLINE_ENGLISH
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.PRICE_CONDITION_GUIDE
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.ROAD_DISTANCE_GUIDE
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.SPEND_TIME_GUIDE
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.THREE_KM
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.THREE_KM_IN_METERS
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.VIA_GUIDE
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.VIA_GUIDE_ENGLISH
import org.techtown.find_gas_station.Util.Parser.OilParser.calOilName
import org.techtown.find_gas_station.Util.Parser.OilParser.calOilSort
import org.techtown.find_gas_station.Util.Parser.OilParser.calRad
import org.techtown.find_gas_station.View.Fragment.HomeFragment
import org.techtown.find_gas_station.ViewModel.SetViewModel
import org.techtown.find_gas_station.databinding.ActivityDrawerBinding

class SettingActivity : AppCompatActivity() {

    private lateinit var mBinding : ActivityDrawerBinding
    private val setViewModel by lazy {
        ViewModelProvider(this, SetViewModel.Factory(application))[SetViewModel::class.java]
    }

    private var priorIntelSetting = mutableListOf("", "", "")
    private var newIntelSetting = mutableListOf("", "", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        settingActivityInit()

        setViewModel.oilLocalData.observe(this) {oilData ->

            if(oilData != null){
                settingInit(priorIntelSetting , oilData)
                settingInit(newIntelSetting , oilData)
                updateUI()

            }

        }


        mBinding.goBack.setOnClickListener {
            endProcess()
        }

        spinnerSet()

    }

    private fun settingInit(setting : MutableList<String> , oilData : OilData) {
        setting[0] = oilData.oilRad
        setting[1] = oilData.oilSort
        setting[2] = oilData.oilName
    }

    private fun spinnerSet() {

        setupSpinner(
            mBinding.typeSpinner,
            listOf(GASOLINE_GUIDE, VIA_GUIDE, PREMIUM_GASOLINE, INDOOR_KEROSENE, CAR_BUTANE)
        ) { selectedValue ->
            newIntelSetting[2] = calOilName(selectedValue)
        }

        setupSpinner(
            mBinding.distanceSpinner,
            listOf(ONE_KM, THREE_KM, FIVE_KM)
        ) { selectedValue ->
            newIntelSetting[0] = calRad(selectedValue)
        }

        setupSpinner(
            mBinding.sortSpinner,
            listOf(PRICE_CONDITION_GUIDE, DIRECT_DISTANCE_GUIDE, ROAD_DISTANCE_GUIDE, SPEND_TIME_GUIDE)
        ) { selectedValue ->
            newIntelSetting[1] = calOilSort(selectedValue)
        }

    }

    private fun settingActivityInit() {
        mBinding = ActivityDrawerBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }


    private fun updateUI() {
        // 기름 종류 설정
        mBinding.typeSpinner.setSelection(when (newIntelSetting[2]) {
            GASOLINE_GUIDE_ENGLISH -> 0 //휘발유
            VIA_GUIDE_ENGLISH -> 1 //경유
            PREMIUM_GASOLINE_ENGLISH -> 2 //고급 휘발유
            INDOOR_KEROSENE_ENGLISH -> 3 //실내 등유
            else -> 4
        })

        // 거리 설정
        mBinding.distanceSpinner.setSelection(when (newIntelSetting[0]) {
            ONE_KM_IN_METERS -> 0
            THREE_KM_IN_METERS -> 1
            else -> 2
        })

        // 정렬 설정
        mBinding.sortSpinner.setSelection(when (newIntelSetting[1]) {
            CHECK_PRICE_CONDITION -> 0
            CHECK_TWO_DIRECT_DISTANCE -> 1
            CHECK_THREE_ROAD_DISTANCE -> 2
            else -> 3
        })

    }



    private fun setupSpinner(spinner: Spinner, options: List<String>, onSelect: (String) -> Unit) {

        val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, options)
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)

        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position : Int, id : Long) {

               (adapterView.getChildAt(0) as TextView).setTextColor(Color.BLACK)
                onSelect(options.getOrElse(position) { "" })
                //updateUI()

            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }

    override fun onBackPressed() {
        endProcess()
    }

    private fun endProcess(){
        HomeFragment.setFlag = true
        if(checkChangeData())
            insertOilData()
        finish()
    }

    private fun checkChangeData() = priorIntelSetting != newIntelSetting

    private fun insertOilData() {

        lifecycleScope.launch(Dispatchers.IO) {

            val job = async {
                setViewModel.delete()
                setViewModel.insert(OilData(newIntelSetting[2], newIntelSetting[0], newIntelSetting[1]))
            }

            job.await()
        }

    }


}