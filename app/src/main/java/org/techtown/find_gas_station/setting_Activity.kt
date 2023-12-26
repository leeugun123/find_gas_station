package org.techtown.find_gas_station

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import org.techtown.find_gas_station.Fragment.HomeFragment
import org.techtown.find_gas_station.MVVM.SetViewModel
import org.techtown.find_gas_station.databinding.ActivityDrawerBinding
import org.techtown.find_gas_station.set.Set

class setting_Activity : AppCompatActivity() {


    private lateinit var mBinding : ActivityDrawerBinding
    private val setViewModel by lazy { ViewModelProvider(this)[SetViewModel::class.java] }

    private var oilIntelSetting = arrayOfNulls<String>(3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingActivityInit()

        setViewModel.setLiveData.observe(this, Observer { set ->
            set?.let {
                oilIntelSetting[0] = set.oil_rad ?: "1000"
                oilIntelSetting[1] = set.oil_sort ?: "1"
                oilIntelSetting[2] = set.oil_name ?: "B027"
                updateUI()
            }
        })

        mBinding.goBack.setOnClickListener {
            HomeFragment.setFlag = true
            finish()
        }

        spinnerSet()

    }

    private fun spinnerSet(){

        setupSpinner(
            mBinding.typeSpinner,
            listOf("휘발유", "경유", "고급 휘발유", "실내 등유", "자동차 부탄")
        ) { oilIntelSetting[2] = it }

        setupSpinner(
            mBinding.distanceSpinner,
            listOf("1km", "3km", "5km")
        ) { oilIntelSetting[0] = it }

        setupSpinner(
            mBinding.sortSpinner,
            listOf("가격순", "직경 거리순", "도로 거리순", "소요시간 순")
        ) { oilIntelSetting[1] = it }

    }

    private fun settingActivityInit() {
        mBinding = ActivityDrawerBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }

    private fun updateUI() {
        // 기름 종류 설정
        mBinding.typeSpinner.setSelection(when (oilIntelSetting[2]) {
            "B027" -> 0
            "D047" -> 1
            "B034" -> 2
            "C004" -> 3
            else -> 4
        })

        // 거리 설정
        mBinding.distanceSpinner.setSelection(when (oilIntelSetting[0]) {
            "1000" -> 0
            "3000" -> 1
            else -> 2
        })

        // 정렬 설정
        mBinding.sortSpinner.setSelection(when (oilIntelSetting[1]) {
            "1" -> 0
            "2" -> 1
            "3" -> 2
            "4" -> 3
            else -> 0 // 기본값으로 설정
        })

    }



    private fun setupSpinner(spinner: Spinner, options: List<String>, onSelect: (String) -> Unit) {

        val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, options)
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)

        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                (adapterView.getChildAt(0) as TextView).setTextColor(Color.BLACK)
                onSelect(options.getOrElse(i) { "" })
                updateUI()
                setViewModel.delete()
                setViewModel.insert(Set(oilIntelSetting[2], oilIntelSetting[0], oilIntelSetting[1]))
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }

    override fun onBackPressed() {
        HomeFragment.setFlag = true
        finish()
    }


}