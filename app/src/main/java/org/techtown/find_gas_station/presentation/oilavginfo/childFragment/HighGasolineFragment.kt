package org.techtown.find_gas_station.presentation.oilavginfo.childFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.databinding.FragmentOilAvgBinding
import org.techtown.find_gas_station.presentation.BaseFragment
import org.techtown.find_gas_station.presentation.oilavginfo.OilAvgViewModel

class HighGasolineFragment : BaseFragment<FragmentOilAvgBinding>(R.layout.fragment_oil_avg){

    private val oilAvgViewModel: OilAvgViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        OilAvgViewCreated().setupOilChartAndRecycler(
            " 고급 휘발유 ",
            requireActivity(),
            binding,
            oilAvgViewModel,
            "B034",
            viewLifecycleOwner
        )
    }

}