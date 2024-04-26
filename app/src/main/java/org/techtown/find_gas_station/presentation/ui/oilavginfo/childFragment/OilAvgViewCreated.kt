package org.techtown.find_gas_station.presentation.ui.oilavginfo.childFragment

import android.content.Context
import android.graphics.Color
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.flow.collect
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.databinding.FragmentOilAvgBinding
import org.techtown.find_gas_station.presentation.ui.oilavginfo.OilAvgRecyclerAdapter
import org.techtown.find_gas_station.presentation.ui.oilavginfo.OilAvgViewModel
import org.techtown.find_gas_station.util.unitconverter.RidRoundMath

class OilAvgViewCreated {

    fun setupOilChartAndRecycler(
        oilKind: String,
        context: Context,
        binding: FragmentOilAvgBinding,
        oilAvgViewModel: OilAvgViewModel,
        oilCode: String,
        viewLifecycleOwner: LifecycleOwner
    ) {

        binding.oilKind.text = oilKind
        binding.oilAvgRecyclerView.layoutManager = LinearLayoutManager(context)

        oilAvgViewModel.requestOilAvg(oilCode)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                oilAvgViewModel.oilAvgInfo.collect { oilAvgPriceInfoList ->

                    val entries = oilAvgPriceInfoList.mapIndexed { index, it ->
                        Entry(index.toFloat(), it.oilPrice.toFloat())
                    }

                    val dataSet =
                        LineDataSet(entries, context.getString(R.string.gas_station_price)).apply {
                            color = Color.rgb(255, 153, 0)
                            lineWidth = 2f
                            setCircleColor(Color.rgb(253, 153, 0))
                            circleRadius = 4f
                            setDrawCircleHole(false)
                        }

                    binding.lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(
                        arrayOf(
                            context.getString(R.string.seven_day_ago),
                            context.getString(R.string.six_day_ago),
                            context.getString(R.string.five_day_ago),
                            context.getString(R.string.four_day_ago),
                            context.getString(R.string.three_day_ago),
                            context.getString(R.string.two_day_ago),
                            context.getString(R.string.one_day_ago)
                        )
                    )

                    val lineData = LineData(dataSet)

                    with(binding.lineChart) {
                        data = lineData
                        description.text = context.getString(R.string.recent_week_price)
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

    }

}