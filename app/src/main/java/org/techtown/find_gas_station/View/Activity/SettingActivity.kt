package org.techtown.find_gas_station.View.Activity

import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
import org.techtown.find_gas_station.Util.OilParser
import org.techtown.find_gas_station.Util.OilParser.calOilName
import org.techtown.find_gas_station.Util.OilParser.calOilSort
import org.techtown.find_gas_station.Util.OilParser.calRad
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
            listOf("휘발유", "경유", "고급 휘발유", "실내 등유", "자동차 부탄")
        ) { selectedValue ->
            newIntelSetting[2] = calOilName(selectedValue)
           // Log.e("TAG",selectedValue)
        }

        setupSpinner(
            mBinding.distanceSpinner,
            listOf("1km", "3km", "5km")
        ) { selectedValue ->
            newIntelSetting[0] = calRad(selectedValue)
           // Log.e("TAG",selectedValue)
        }

        setupSpinner(
            mBinding.sortSpinner,
            listOf("가격순", "직경 거리순", "도로 거리순", "소요 시간순")
        ) { selectedValue ->
            newIntelSetting[1] = calOilSort(selectedValue)
            //Log.e("TAG",selectedValue)
        }
    }

    private fun settingActivityInit() {
        mBinding = ActivityDrawerBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }


    private fun updateUI() {
        // 기름 종류 설정
        mBinding.typeSpinner.setSelection(when (newIntelSetting[2]) {
            "B027" -> 0
            "D047" -> 1
            "B034" -> 2
            "C004" -> 3
            else -> 4
        })

        // 거리 설정
        mBinding.distanceSpinner.setSelection(when (newIntelSetting[0]) {
            "1000" -> 0
            "3000" -> 1
            else -> 2
        })

        // 정렬 설정
        mBinding.sortSpinner.setSelection(when (newIntelSetting[1]) {
            "1" -> 0
            "2" -> 1
            "3" -> 2
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