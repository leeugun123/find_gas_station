package org.techtown.find_gas_station.presentation.oilavginfo

import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.databinding.FragmentDailyBinding
import org.techtown.find_gas_station.presentation.BaseFragment
import org.techtown.find_gas_station.presentation.oilavginfo.childFragment.ButaneFragment
import org.techtown.find_gas_station.presentation.oilavginfo.childFragment.DieselFragment
import org.techtown.find_gas_station.presentation.oilavginfo.childFragment.GasolineFragment
import org.techtown.find_gas_station.presentation.oilavginfo.childFragment.HighGasolineFragment
import org.techtown.find_gas_station.presentation.oilavginfo.childFragment.KeroseneFragment

class DailyFragment : BaseFragment<FragmentDailyBinding>(R.layout.fragment_daily) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        connectPagerAdapter()
        controlTabLayOut()
    }

    private fun connectPagerAdapter() {
        binding.oilAvgViewPager.adapter = OilAvgPagerAdapter(
            childFragmentManager,
            lifecycle,
            listOf(
                GasolineFragment(),
                DieselFragment(),
                HighGasolineFragment(),
                KeroseneFragment(),
                ButaneFragment()
            )
        )
    }

    private fun controlTabLayOut() {
        TabLayoutMediator(binding.oilAvgtabs, binding.oilAvgViewPager) { tab, position ->
            tab.text = when (position) {
                0 -> requireContext().getString(R.string.gasoline)
                1 -> requireContext().getString(R.string.diesel_oil)
                2 -> requireContext().getString(R.string.premium_gasoline_line)
                3 -> requireContext().getString(R.string.indoor_kerosene)
                4 -> requireContext().getString(R.string.car_butane_line)
                else -> throw IllegalArgumentException("Invalid position")
            }
        }.attach()
    }
}
