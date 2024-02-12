package org.techtown.find_gas_station.View.Fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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
import kotlinx.coroutines.withContext
import org.techtown.find_gas_station.Adapter.OilAvgRecyclerAdapter
import org.techtown.find_gas_station.Util.UnitConverter.RidRoundMath
import org.techtown.find_gas_station.ViewModel.GetOilAvgViewModel
import org.techtown.find_gas_station.databinding.FragmentKeroseneBinding

class KeroseneFragment : Fragment() {

    private val getOilAvgViewModel by lazy { ViewModelProvider(this)[GetOilAvgViewModel::class.java] }
    private lateinit var mBinding : FragmentKeroseneBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentKeroseneBinding.inflate(layoutInflater, container,false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.oilAvgRecyclerView.layoutManager = LinearLayoutManager(requireActivity())

        lifecycleScope.launch(Dispatchers.Main) {
            getOilAvgViewModel.requestOilAvg("C004")
        }

        getOilAvgViewModel.getOilAvg().observe(requireActivity()) { oilAvgPriceInfoList ->
            val entries = oilAvgPriceInfoList.mapIndexed { index, it ->
                Entry(index.toFloat(), it.oilPrice.toFloat())
            }

            val dataSet = LineDataSet(entries, "주유소 가격").apply {
                color = Color.rgb(255, 153, 0)
                lineWidth = 2f
                setCircleColor(Color.rgb(253, 153, 0))
                circleRadius = 4f
                setDrawCircleHole(false)
            }

            mBinding.lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(
                arrayOf("7일전", "6일전", "5일전", "4일전", "3일전", "2일전", "1일전")
            )

            val lineData = LineData(dataSet)

            with(mBinding.lineChart) {
                data = lineData
                description.text = "최근 일주일 전국 유가 가격"
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                axisRight.isEnabled = false
                invalidate()
            }

            oilAvgPriceInfoList.reversed()

            if (oilAvgPriceInfoList.isNotEmpty()) {
                mBinding.priceText.text =
                    RidRoundMath.roundStringToInteger(oilAvgPriceInfoList.first().oilPrice)
                        .toString()
            }

            mBinding.oilAvgRecyclerView.adapter = OilAvgRecyclerAdapter(oilAvgPriceInfoList)

        }

    }

}