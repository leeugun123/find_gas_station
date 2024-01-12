package org.techtown.find_gas_station.View.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.databinding.FragmentDailyBinding

class DailyFragment : Fragment() {

    private val gasolFragment by lazy { GasolineFragment() }
    private val diselFragment by lazy { DieselFragment() }
    private val highGasolFragment by lazy { High_GasolineFragment() }
    private val keroseneFragment by lazy { KeroseneFragment() }
    private val butanFragment by lazy { ButaneFragment() }

    private lateinit var mBinding : FragmentDailyBinding
    private lateinit var currentFragment : Fragment
    // 현재 보여지고 있는 프래그먼트를 저장하는 변수
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentFragment = gasolFragment
        parentFragmentManager.beginTransaction().add(R.id.frame, currentFragment).commit()


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mBinding = FragmentDailyBinding.inflate(layoutInflater, container, false)

        mBinding.tabs.addOnTabSelectedListener(object : OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab) {

                val selectedFragment = when (tab.position) {
                    0 -> gasolFragment
                    1 -> diselFragment
                    2 -> highGasolFragment
                    3 -> keroseneFragment
                    else -> butanFragment
                }

                if (currentFragment === selectedFragment) {
                    return
                }

                val transaction = parentFragmentManager.beginTransaction()

                if (!selectedFragment.isAdded) {
                    transaction.add(R.id.frame, selectedFragment)
                } else {
                    transaction.show(selectedFragment)
                }

                transaction.hide(currentFragment).commit()
                currentFragment = selectedFragment

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        return mBinding.root

    }

}