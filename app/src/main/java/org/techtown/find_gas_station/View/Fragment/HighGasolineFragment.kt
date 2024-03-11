package org.techtown.find_gas_station.View.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import org.techtown.find_gas_station.OilAvgViewCreated
import org.techtown.find_gas_station.ViewModel.GetOilAvgViewModel
import org.techtown.find_gas_station.databinding.FragmentOilAvgBinding

class HighGasolineFragment : Fragment() {

    private val getOilAvgViewModel by viewModels<GetOilAvgViewModel>()
    private lateinit var mBinding : FragmentOilAvgBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        mBinding = FragmentOilAvgBinding.inflate(layoutInflater, container,false)
        return mBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        OilAvgViewCreated().setupOilChartAndRecycler(" 고급 휘발유 ",requireActivity() , mBinding , getOilAvgViewModel, "B034")

    }

}