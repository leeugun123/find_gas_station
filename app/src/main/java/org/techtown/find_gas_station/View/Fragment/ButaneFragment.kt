package org.techtown.find_gas_station.View.Fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.techtown.find_gas_station.Adapter.OilAvgRecyclerAdapter
import org.techtown.find_gas_station.OilAvgViewCreated
import org.techtown.find_gas_station.Util.UnitConverter.RidRoundMath
import org.techtown.find_gas_station.ViewModel.GetOilAvgViewModel
import org.techtown.find_gas_station.databinding.FragmentOilAvgBinding

class ButaneFragment : Fragment() {

    private val getOilAvgViewModel by viewModels<GetOilAvgViewModel>()
    private lateinit var mBinding : FragmentOilAvgBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        mBinding = FragmentOilAvgBinding.inflate(layoutInflater, container,false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        OilAvgViewCreated().setupOilChartAndRecycler(" 자동차 부탄 ",requireActivity() , mBinding , getOilAvgViewModel, "K015")

    }

}