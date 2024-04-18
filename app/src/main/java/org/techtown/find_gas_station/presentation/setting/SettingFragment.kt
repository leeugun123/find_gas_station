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
           // val selectedItem = adapterView.getItemAtPosition(position).toString()
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


    private fun goBack() {
        findNavController().popBackStack()
    }
}