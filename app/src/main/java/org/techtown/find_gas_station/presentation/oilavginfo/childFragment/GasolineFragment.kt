package org.techtown.find_gas_station.presentation.oilavginfo.childFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.databinding.FragmentOilAvgBinding
import org.techtown.find_gas_station.presentation.BaseFragment
import org.techtown.find_gas_station.presentation.oilavginfo.OilAvgViewCreated
import org.techtown.find_gas_station.presentation.oilavginfo.OilAvgViewModel

class GasolineFragment : BaseFragment<FragmentOilAvgBinding>(R.layout.fragment_oil_avg) {

    private val oilAvgViewModel: OilAvgViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        OilAvgViewCreated().setupOilChartAndRecycler(
            " 휘발유 ",
            requireContext(),
            binding,
            oilAvgViewModel,
            "B027",
            viewLifecycleOwner
        )
    }
}