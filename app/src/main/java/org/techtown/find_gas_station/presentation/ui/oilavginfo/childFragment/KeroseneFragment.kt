package org.techtown.find_gas_station.presentation.ui.oilavginfo.childFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.databinding.FragmentOilAvgBinding
import org.techtown.find_gas_station.presentation.common.base.BaseFragment
import org.techtown.find_gas_station.presentation.ui.oilavginfo.OilAvgViewModel

class KeroseneFragment(private val oilAvgViewModel : OilAvgViewModel) : BaseFragment<FragmentOilAvgBinding>(R.layout.fragment_oil_avg) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOilAvgViewCreated()
    }

    private fun setOilAvgViewCreated() {
        OilAvgViewCreated.setupOilChartAndRecycler(
            requireContext().getString(R.string.indoor_kerosene),
            requireContext(),
            binding,
            oilAvgViewModel,
            requireContext().getString(R.string.indoor_kerosene_code),
            viewLifecycleOwner
        )
    }
}