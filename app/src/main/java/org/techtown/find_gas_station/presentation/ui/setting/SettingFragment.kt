package org.techtown.find_gas_station.presentation.ui.setting

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.databinding.FragmentSettingBinding
import org.techtown.find_gas_station.presentation.common.base.BaseFragment
import org.techtown.find_gas_station.presentation.ui.oilroundinfo.StationInfoViewModel

@AndroidEntryPoint
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
            (adapterView.getChildAt(0) as? TextView)?.textSize = 15.0F

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

    private fun initBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = stationInfoViewModel
        binding.goBackClick = ::goBack
        binding.typeSpinner.onItemSelectedListener = onItemSelectedListener
        binding.distanceSpinner.onItemSelectedListener = onItemSelectedListener
        binding.sortSpinner.onItemSelectedListener = onItemSelectedListener
    }

    private fun goBack() {
        stationInfoViewModel.isConditionChange = checkCondition()
        findNavController().popBackStack()
    }

    private fun checkCondition() =
        stationInfoViewModel.oilCondition.sort != stationInfoViewModel.afterOilCondition.sort
                    || stationInfoViewModel.oilCondition.radius != stationInfoViewModel.afterOilCondition.radius
                    || stationInfoViewModel.oilCondition.oilKind != stationInfoViewModel.afterOilCondition.oilKind

            private fun initCheckConditionChange() {
                stationInfoViewModel.afterOilCondition = stationInfoViewModel.oilCondition.copy()
            }

            private fun changeValue(selectedItem: String) {
                when (selectedItem) {
                    requireContext().getString(R.string.gasoline) -> stationInfoViewModel.oilCondition.oilKind =
                        requireContext().getString(R.string.gasoline_code)

                    requireContext().getString(R.string.diesel_oil) -> stationInfoViewModel.oilCondition.oilKind =
                        requireContext().getString(R.string.diesel_oil_code)

                    requireContext().getString(R.string.premium_gasoline) -> stationInfoViewModel.oilCondition.oilKind =
                requireContext().getString(R.string.premium_gasoline_code)

            requireContext().getString(R.string.indoor_kerosene) -> stationInfoViewModel.oilCondition.oilKind =
                requireContext().getString(R.string.indoor_kerosene_code)

            requireContext().getString(R.string.car_butane) -> stationInfoViewModel.oilCondition.oilKind =
                requireContext().getString(R.string.car_butane_code)

            requireContext().getString(R.string.one_km) -> stationInfoViewModel.oilCondition.radius =
                requireContext().getString(R.string.one_km_number)

            requireContext().getString(R.string.three_km) -> stationInfoViewModel.oilCondition.radius =
                requireContext().getString(R.string.three_km_number)

            requireContext().getString(R.string.five_km) -> stationInfoViewModel.oilCondition.radius =
                requireContext().getString(R.string.five_km_number)

            requireContext().getString(R.string.sort_price) -> stationInfoViewModel.oilCondition.sort =
                requireContext().getString(R.string.one)

            requireContext().getString(R.string.sort_direct_distance) -> stationInfoViewModel.oilCondition.sort =
                requireContext().getString(R.string.two)

            requireContext().getString(R.string.sort_road_distance) -> stationInfoViewModel.oilCondition.sort =
                requireContext().getString(R.string.three)

            requireContext().getString(R.string.sort_spend_time) -> stationInfoViewModel.oilCondition.sort =
                requireContext().getString(R.string.four)
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

    private fun setAdapterSelection() {
        setOilKindAdapterSelection()
        setDistanceAdapterSelection()
        setSortAdapterSelection()
    }

    private fun setOilKindAdapterSelection() {
        when (stationInfoViewModel.oilCondition.oilKind) {
            requireContext().getString(R.string.gasoline_code) -> binding.typeSpinner.setSelection(0)
            requireContext().getString(R.string.diesel_oil_code) -> binding.typeSpinner.setSelection(1)
            requireContext().getString(R.string.premium_gasoline_code) -> binding.typeSpinner.setSelection(2)
            requireContext().getString(R.string.indoor_kerosene_code) -> binding.typeSpinner.setSelection(3)
            requireContext().getString(R.string.car_butane_code) -> binding.typeSpinner.setSelection(4)
        }
    }

    private fun setDistanceAdapterSelection() {
            when (stationInfoViewModel.oilCondition.radius) {
                requireContext().getString(R.string.one_km_number) -> binding.distanceSpinner.setSelection(0)
                requireContext().getString(R.string.three_km_number) -> binding.distanceSpinner.setSelection(1)
                requireContext().getString(R.string.five_km_number) -> binding.distanceSpinner.setSelection(2)
        }
    }

    private fun setSortAdapterSelection() {
        when (stationInfoViewModel.oilCondition.sort) {
            requireContext().getString(R.string.one) -> sortSpinnerSelection(0)
            requireContext().getString(R.string.two) -> sortSpinnerSelection(1)
            requireContext().getString(R.string.three) -> sortSpinnerSelection(2)
            requireContext().getString(R.string.four) -> sortSpinnerSelection(3)
        }
    }

    private fun sortSpinnerSelection(idx: Int) {
        binding.sortSpinner.setSelection(idx)
    }
}