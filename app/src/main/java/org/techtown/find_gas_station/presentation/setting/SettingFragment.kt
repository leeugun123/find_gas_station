package org.techtown.find_gas_station.presentation.setting

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.activityViewModels
import org.techtown.find_gas_station.BaseFragment
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.databinding.FragmentSettingBinding
import org.techtown.find_gas_station.presentation.oilroundinfo.OilInfoViewModel

class SettingFragment() : BaseFragment<FragmentSettingBinding>(R.layout.fragment_setting) {

    private val oilInfoViewModel: OilInfoViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBinding()
        setSelectAdapter()
    }

    private fun initBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = oilInfoViewModel
        binding.goBackClick = ::goBack
    }

    private fun setSelectAdapter() {
        initSpinner(binding.typeSpinner, R.array.oil_Type_Items)
        initSpinner(binding.distanceSpinner, R.array.distance_Type_Items)
        initSpinner(binding.sortSpinner, R.array.sort_Items)
    }

    fun initSpinner(spinner: Spinner, itemsArrayResId: Int) {
        ArrayAdapter.createFromResource(
            requireContext(),
            itemsArrayResId,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    private fun goBack() {
        //findNavController().popBackStack()
    }
}