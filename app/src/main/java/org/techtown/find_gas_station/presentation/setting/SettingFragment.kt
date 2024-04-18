package org.techtown.find_gas_station.presentation.setting

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import org.techtown.find_gas_station.BaseFragment
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.databinding.FragmentSettingBinding
import org.techtown.find_gas_station.presentation.oilroundinfo.OilInfoViewModel

class SettingFragment() : BaseFragment<FragmentSettingBinding>(R.layout.fragment_setting) {

    private val oilInfoViewModel: OilInfoViewModel by activityViewModels()

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

    }

    private fun initBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = oilInfoViewModel
        binding.goBackClick = ::goBack
        binding.typeSpinner.onItemSelectedListener = onItemSelectedListener
        binding.distanceSpinner.onItemSelectedListener = onItemSelectedListener
        binding.sortSpinner.onItemSelectedListener = onItemSelectedListener
    }

    private fun changeValue(selectedItem: String) {
        when (selectedItem) {
            "휘발유" -> oilInfoViewModel.oilCondition.oilKind = "B027"
            "경유" -> oilInfoViewModel.oilCondition.oilKind = "D047"
            "고급 휘발유" -> oilInfoViewModel.oilCondition.oilKind = "B034"
            "실내 등유" -> oilInfoViewModel.oilCondition.oilKind = "C004"
            "자동차 부탄" -> oilInfoViewModel.oilCondition.oilKind = "K015"
            "1km" -> oilInfoViewModel.oilCondition.radius = "1000"
            "3km" -> oilInfoViewModel.oilCondition.radius = "3000"
            "5km" -> oilInfoViewModel.oilCondition.radius = "5000"
            "가격순" -> oilInfoViewModel.oilCondition.sort = "1"
            "직경 거리순" -> oilInfoViewModel.oilCondition.sort = "2"
            "도로 거리순" -> oilInfoViewModel.oilCondition.sort = "3"
            "소요 시간순" -> oilInfoViewModel.oilCondition.sort = "4"
        }
    }

    private fun goBack() {
        findNavController().popBackStack()
    }
}