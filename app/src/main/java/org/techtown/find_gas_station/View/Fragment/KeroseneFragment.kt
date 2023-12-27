package org.techtown.find_gas_station.View.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import org.techtown.find_gas_station.ViewModel.GetOilViewModel
import org.techtown.find_gas_station.databinding.FragmentKeroseneBinding

class KeroseneFragment : Fragment() {

    private val getOilViewModel by lazy { ViewModelProvider(this)[GetOilViewModel::class.java] }
    private lateinit var mBinding : FragmentKeroseneBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mBinding = FragmentKeroseneBinding.inflate(layoutInflater, container,false)
        mBinding.oilAvgRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        getOilViewModel!!.getOilAvg(mBinding.lineChart, mBinding.oilAvgRecyclerView, mBinding.priceText, "C004")
        return mBinding.root

    }

}