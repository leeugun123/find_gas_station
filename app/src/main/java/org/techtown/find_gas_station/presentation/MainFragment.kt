package org.techtown.find_gas_station.presentation

import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.databinding.FragmentMainBinding
import org.techtown.find_gas_station.presentation.ui.oilavginfo.DailyFragment
import org.techtown.find_gas_station.presentation.ui.oilroundinfo.StationInfoFragment
import org.techtown.find_gas_station.presentation.ui.BaseFragment


@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {

    private val stationInfoFragment by lazy { StationInfoFragment() }
    private val dailyFragment by lazy { DailyFragment() }
    private val homeFragmentManager by lazy { childFragmentManager }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initChildFragment()
        initBottomNavigationBar()
    }

    private fun initChildFragment() {
        addFragments()
    }

    private fun addFragments() {

        if (homeFragmentManager.findFragmentByTag(OIL_FRAGMENT_TAG) != null ||
            homeFragmentManager.findFragmentByTag(DAILY_FRAGMENT_TAG) != null
        )
            return

        homeFragmentManager.beginTransaction()
            .add(R.id.main_frame, stationInfoFragment, OIL_FRAGMENT_TAG).commit()
        homeFragmentManager.beginTransaction()
            .add(R.id.main_frame, dailyFragment, DAILY_FRAGMENT_TAG).commit()
    }

    private fun initBottomNavigationBar() =
        binding.bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.station_fragment -> {
                    showStationInfoFragment()
                }

                R.id.Daily_fragment -> {
                    showDailyFragment()
                }

                else -> throw IllegalArgumentException(INVALID_ID)
            }
            true
        }

    private fun showStationInfoFragment() {
        homeFragmentManager.beginTransaction().show(stationInfoFragment).commit()
        homeFragmentManager.beginTransaction().hide(dailyFragment).commit()
    }

    private fun showDailyFragment() {
        homeFragmentManager.beginTransaction().show(dailyFragment).commit()
        homeFragmentManager.beginTransaction().hide(stationInfoFragment).commit()
    }

    companion object {
        private const val OIL_FRAGMENT_TAG = "OilInfoFragment"
        private const val DAILY_FRAGMENT_TAG = "DailyFragment"
        private const val INVALID_ID = "Invalid itemId"
    }
}