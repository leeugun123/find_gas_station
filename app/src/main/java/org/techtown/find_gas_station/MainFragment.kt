package org.techtown.find_gas_station

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import org.techtown.find_gas_station.databinding.FragmentMainBinding
import org.techtown.find_gas_station.localdatabase.OilData
import org.techtown.find_gas_station.localdatabase.SetViewModel
import org.techtown.find_gas_station.presentation.oilavginfo.DailyFragment
import org.techtown.find_gas_station.presentation.oilroundinfo.HomeFragment
import org.techtown.find_gas_station.presentation.oilroundinfo.OilCondition
import org.techtown.find_gas_station.util.constant.ConstantGuide
import org.techtown.find_gas_station.util.constant.ConstantsTime


class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {

    private val fa by lazy { HomeFragment() }
    private val fb by lazy { DailyFragment() }
    private val homeFragmentManager by lazy { childFragmentManager }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentInit()
        bottomNavigationBarInit()
    }

    private fun bottomNavigationBarInit() =
        binding.bottomNav.setOnItemSelectedListener { menuItem ->

            when (menuItem.itemId) {
                R.id.Home_fragment -> {
                    showFa()
                }

                R.id.Daily_fragment -> {
                    showFb()
                }

                else -> throw IllegalArgumentException(ConstantGuide.INVALID_GUIDE)
            }
            true
        }

    private fun showFa() {
        homeFragmentManager.beginTransaction().show(fa).commit()
        homeFragmentManager.beginTransaction().hide(fb).commit()
    }

    private fun showFb() {
        homeFragmentManager.beginTransaction().show(fb).commit()
        homeFragmentManager.beginTransaction().hide(fa).commit()
    }

    private fun fragmentInit() {
        addFragment()
    }

    private fun addFragment() {
        homeFragmentManager.beginTransaction().add(R.id.main_frame, fa).commit()
        homeFragmentManager.beginTransaction().add(R.id.main_frame, fb).commit()
    }

}