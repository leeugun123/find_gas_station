package org.techtown.find_gas_station.View.Fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import org.techtown.find_gas_station.Adapter.OilAvgRecyclerAdapter
import org.techtown.find_gas_station.ViewModel.GetOilAvgViewModel
import org.techtown.find_gas_station.databinding.FragmentButaneBinding

class ButaneFragment : Fragment() {

    private val getOilAvgViewModel by lazy { ViewModelProvider(this)[GetOilAvgViewModel::class.java] }
    private lateinit var mBinding : FragmentButaneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        mBinding = FragmentButaneBinding.inflate(layoutInflater, container,false)
        mBinding.oilAvgRecyclerView.layoutManager = LinearLayoutManager(requireActivity())

        getOilAvgViewModel.requestOilAvg("K015")

        getOilAvgViewModel.getOilAvg().observe(requireActivity() , Observer { oilAvgPriceInfoList ->

            val entries = mutableListOf<Entry>()

            oilAvgPriceInfoList.forEachIndexed { index , it ->
                entries.add(Entry(index.toFloat(), it.oilPrice.toFloat()))
            }

            val dataSet = LineDataSet(entries, "주유소 가격")
            dataSet.color = Color.rgb(255, 153, 0)
            dataSet.lineWidth = 2f
            dataSet.setCircleColor(Color.rgb(253, 153, 0)) // 수정된 부분
            dataSet.circleRadius = 4f
            dataSet.setDrawCircleHole(false)


            mBinding.lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(
                arrayOf("7일전", "6일전", "5일전", "4일전", "3일전", "2일전", "1일전")
            )

            val dataSets = mutableListOf<ILineDataSet>()
            dataSets.add(dataSet)

            val lineData = LineData(dataSets)

            mBinding.lineChart.data = lineData
            mBinding.lineChart.description.text = "최근 일주일 전국 유가 가격"
            mBinding.lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
            mBinding.lineChart.axisRight.isEnabled = false
            mBinding.lineChart.invalidate()

            oilAvgPriceInfoList.reversed()

            if (oilAvgPriceInfoList.isNotEmpty()) {
                mBinding.priceText.text = oilAvgPriceInfoList[0].oilPrice
            }

            mBinding.oilAvgRecyclerView.adapter = OilAvgRecyclerAdapter(oilAvgPriceInfoList)


        })


        return mBinding.root

    }

}