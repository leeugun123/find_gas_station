package org.techtown.find_gas_station

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.techtown.find_gas_station.databinding.FragmentMainBinding
import org.techtown.find_gas_station.localdatabase.SetViewModel
import org.techtown.find_gas_station.presentation.oilavginfo.DailyFragment
import org.techtown.find_gas_station.util.constant.ConstantGuide


@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {

    private val oilInfoFragment by lazy { OilInfoFragment() }
    private val dailyFragment by lazy { DailyFragment() }
    private val homeFragmentManager by lazy { childFragmentManager }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentInit()
        bottomNavigationBarInit()
    }

    private fun bottomNavigationBarInit() =
        binding.bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.Home_fragment -> {
                    showOilInfoFragment()
                }

                R.id.Daily_fragment -> {
                    showDailyFragment()
                }

                else -> throw IllegalArgumentException(ConstantGuide.INVALID_GUIDE)
            }
            true
        }

    private fun showOilInfoFragment() {
        homeFragmentManager.beginTransaction().show(oilInfoFragment).commit()
        homeFragmentManager.beginTransaction().hide(dailyFragment).commit()
    }

    private fun showDailyFragment() {
        homeFragmentManager.beginTransaction().show(dailyFragment).commit()
        homeFragmentManager.beginTransaction().hide(oilInfoFragment).commit()
    }

    private fun childFragmentInit() {
        addFragment()
    }

    private fun addFragment() {
        homeFragmentManager.beginTransaction().add(R.id.main_frame, oilInfoFragment).commit()
        homeFragmentManager.beginTransaction().add(R.id.main_frame, dailyFragment).commit()
    }

}