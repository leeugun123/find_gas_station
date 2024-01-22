package org.techtown.find_gas_station.View.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import org.techtown.find_gas_station.Adapter.OilAvgPagerAdapter
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.CAR_BUTANE_KOREAN
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.GASOLINE_KOREAN
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.INDOOR_KEROSENE_KOREAN
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.PREMIUM_GASOLINE_KOREAN
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.VIA_KOREAN
import org.techtown.find_gas_station.databinding.FragmentDailyBinding

class DailyFragment : Fragment() {

    private lateinit var mBinding: FragmentDailyBinding
    private lateinit var viewPager : ViewPager2

    private val pagerAdapter by lazy {
        OilAvgPagerAdapter(
            childFragmentManager,
            lifecycle,
            listOf(GasolineFragment(), DieselFragment(), High_GasolineFragment(), KeroseneFragment(), ButaneFragment())
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentDailyBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = mBinding.oilAvgViewPager
        viewPager.adapter = pagerAdapter

        TabLayoutMediator(mBinding.oilAvgtabs, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> GASOLINE_KOREAN
                1 -> VIA_KOREAN
                2 -> PREMIUM_GASOLINE_KOREAN
                3 -> INDOOR_KEROSENE_KOREAN
                4 -> CAR_BUTANE_KOREAN
                else -> throw IllegalArgumentException("Invalid position")
            }
        }.attach()

    }

}
