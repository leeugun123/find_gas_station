package org.techtown.find_gas_station.presentation.oilavginfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.databinding.FragmentDailyBinding

class DailyFragment : Fragment() {

    private lateinit var mBinding: FragmentDailyBinding
    private val viewPager by lazy { mBinding.oilAvgViewPager }

    private val pagerAdapter by lazy {
        OilAvgPagerAdapter(
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentDailyBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        connectPagerAdapter()
        controlTabLayOut()
    }

    private fun controlTabLayOut() {
        TabLayoutMediator(mBinding.oilAvgtabs, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> requireContext().getString(R.string.gasoline)
                1 -> requireContext().getString(R.string.diesel_oil)
                2 -> requireContext().getString(R.string.premium_gasoline)
                3 -> requireContext().getString(R.string.indoor_kerosene)
                4 -> requireContext().getString(R.string.car_butane)
                else -> throw IllegalArgumentException("Invalid position")
            }
        }.attach()
    }

    private fun connectPagerAdapter() {
        viewPager.adapter = pagerAdapter
    }

}
