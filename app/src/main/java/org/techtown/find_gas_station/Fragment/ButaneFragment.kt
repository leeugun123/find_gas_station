package org.techtown.find_gas_station.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import org.techtown.find_gas_station.MVVM.GetOilViewModel
import org.techtown.find_gas_station.databinding.FragmentButaneBinding

class ButaneFragment : Fragment() {

    private val getOilViewModel by lazy { ViewModelProvider(this)[GetOilViewModel::class.java] }
    private lateinit var mBinding : FragmentButaneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        mBinding = FragmentButaneBinding.inflate(layoutInflater, container,false)
        mBinding.oilAvgRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        getOilViewModel!!.getOilAvg(mBinding.lineChart, mBinding.oilAvgRecyclerView, mBinding.priceText, "K015")
        return mBinding.root

    }

}