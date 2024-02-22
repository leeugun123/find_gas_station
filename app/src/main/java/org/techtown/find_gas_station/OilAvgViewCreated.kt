package org.techtown.find_gas_station

import android.content.Context
import android.graphics.Color
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.techtown.find_gas_station.Adapter.OilAvgRecyclerAdapter
import org.techtown.find_gas_station.Util.UnitConverter.RidRoundMath
import org.techtown.find_gas_station.ViewModel.GetOilAvgViewModel
import org.techtown.find_gas_station.databinding.FragmentOilAvgBinding

class OilAvgViewCreated {

    fun setupOilChartAndRecycler(
        oilKind : String,
        fragmentActivity : FragmentActivity,
        binding : FragmentOilAvgBinding,
        getOilAvgViewModel: GetOilAvgViewModel,
        oilCode: String
    ) {
        binding.oilKind.text = oilKind

        binding.oilAvgRecyclerView.layoutManager = LinearLayoutManager(fragmentActivity)

        getOilAvgViewModel.requestOilAvg(oilCode)

        getOilAvgViewModel.oilAvgLiveData.observe(fragmentActivity) { oilAvgPriceInfoList ->
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

            binding.lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(
                arrayOf("7일전", "6일전", "5일전", "4일전", "3일전", "2일전", "1일전")
            )

            val lineData = LineData(dataSet)

            with(binding.lineChart) {
                data = lineData
                description.text = "최근 일주일 전국 유가 가격"
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                axisRight.isEnabled = false
                invalidate()
            }

            oilAvgPriceInfoList.reversed()

            if (oilAvgPriceInfoList.isNotEmpty()) {
                binding.priceText.text =
                    RidRoundMath.roundStringToInteger(oilAvgPriceInfoList.last().oilPrice)
                        .toString()
            }

            binding.oilAvgRecyclerView.adapter = OilAvgRecyclerAdapter(oilAvgPriceInfoList)
        }

    }



}