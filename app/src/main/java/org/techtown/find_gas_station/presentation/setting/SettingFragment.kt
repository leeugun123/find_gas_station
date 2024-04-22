package org.techtown.find_gas_station.presentation.setting

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.databinding.FragmentSettingBinding
import org.techtown.find_gas_station.presentation.BaseFragment
import org.techtown.find_gas_station.presentation.oilroundinfo.StationInfoViewModel

class SettingFragment() : BaseFragment<FragmentSettingBinding>(R.layout.fragment_setting) {

    private val stationInfoViewModel: StationInfoViewModel by activityViewModels()

    private val onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            adapterView: AdapterView<*>,
            view: View,
            position: Int,
            id: Long
        ) {
            (adapterView.getChildAt(0) as? TextView)?.setTextColor(Color.BLACK)
            val selectedItem = adapterView.getItemAtPosition(position).toString()
            changeValue(selectedItem)
        }

        override fun onNothingSelected(adapterView: AdapterView<*>?) {}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
        initCheckConditionChange()
        initOnBackPressed()

        setAdapterSelection()
    }

    private fun initCheckConditionChange() {
        stationInfoViewModel.conditionChangeFlag = false
        stationInfoViewModel.afterOilCondition = stationInfoViewModel.oilCondition.copy()
    }

    private fun initBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = stationInfoViewModel
        binding.goBackClick = ::goBack
        binding.typeSpinner.onItemSelectedListener = onItemSelectedListener
        binding.distanceSpinner.onItemSelectedListener = onItemSelectedListener
        binding.sortSpinner.onItemSelectedListener = onItemSelectedListener
    }

    private fun setAdapterSelection() {
        setOilKindAdapterSelection()
        setDistanceAdapterSelection()
        setSortAdapterSelection()
    }

    private fun setOilKindAdapterSelection() {
        when (stationInfoViewModel.oilCondition.oilKind) {
            "B027" -> typeSpinnerSelection(0)
            "D047" -> typeSpinnerSelection(1)
            "B034" -> typeSpinnerSelection(2)
            "C004" -> typeSpinnerSelection(3)
            "K015" -> typeSpinnerSelection(4)
        }
    }

    private fun setDistanceAdapterSelection() {
        when (stationInfoViewModel.oilCondition.radius) {
            "1000" -> distanceSpinnerSelection(0)
            "3000" -> distanceSpinnerSelection(1)
            "5000" -> distanceSpinnerSelection(2)
        }
    }

    private fun setSortAdapterSelection() {
        when (stationInfoViewModel.oilCondition.sort) {
            "1" -> sortSpinnerSelection(0)
            "2" -> sortSpinnerSelection(1)
            "3" -> sortSpinnerSelection(2)
            "4" -> sortSpinnerSelection(3)
        }
    }

    private fun typeSpinnerSelection(idx: Int) {
        binding.typeSpinner.setSelection(idx)
    }

    private fun distanceSpinnerSelection(idx: Int) {
        binding.distanceSpinner.setSelection(idx)
    }

    private fun sortSpinnerSelection(idx: Int) {
        binding.sortSpinner.setSelection(idx)
    }

    private fun changeValue(selectedItem: String) {
        when (selectedItem) {
            "휘발유" -> stationInfoViewModel.oilCondition.oilKind = "B027"
            "경유" -> stationInfoViewModel.oilCondition.oilKind = "D047"
            "고급 휘발유" -> stationInfoViewModel.oilCondition.oilKind = "B034"
            "실내 등유" -> stationInfoViewModel.oilCondition.oilKind = "C004"
            "자동차 부탄" -> stationInfoViewModel.oilCondition.oilKind = "K015"
            "1km" -> stationInfoViewModel.oilCondition.radius = "1000"
            "3km" -> stationInfoViewModel.oilCondition.radius = "3000"
            "5km" -> stationInfoViewModel.oilCondition.radius = "5000"
            "가격순" -> stationInfoViewModel.oilCondition.sort = "1"
            "직경 거리순" -> stationInfoViewModel.oilCondition.sort = "2"
            "도로 거리순" -> stationInfoViewModel.oilCondition.sort = "3"
            "소요 시간순" -> stationInfoViewModel.oilCondition.sort = "4"
        }
    }

    private fun initOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    goBack()
                }
            })
    }

    private fun goBack() {
        checkChange()
        findNavController().popBackStack()
    }

    private fun checkChange() {
        if (stationInfoViewModel.oilCondition.sort != stationInfoViewModel.afterOilCondition.sort
            || stationInfoViewModel.oilCondition.radius != stationInfoViewModel.afterOilCondition.radius
            || stationInfoViewModel.oilCondition.oilKind != stationInfoViewModel.afterOilCondition.oilKind
        )
            stationInfoViewModel.conditionChangeFlag = true
    }
}