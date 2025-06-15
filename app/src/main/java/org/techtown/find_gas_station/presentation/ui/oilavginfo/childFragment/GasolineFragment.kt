package org.techtown.find_gas_station.presentation.ui.oilavginfo.childFragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.databinding.FragmentOilAvgBinding
import org.techtown.find_gas_station.domain.model.OilAveragePriceInfo
import org.techtown.find_gas_station.presentation.common.base.BaseFragment
import org.techtown.find_gas_station.presentation.common.util.DateConverterUtil
import org.techtown.find_gas_station.presentation.common.util.convertor.RidRoundMath
import org.techtown.find_gas_station.presentation.ui.oilavginfo.OilAvgRecyclerAdapter
import org.techtown.find_gas_station.presentation.ui.oilavginfo.OilAvgViewModel
import javax.inject.Inject

@AndroidEntryPoint
class GasolineFragment : BaseFragment<FragmentOilAvgBinding>(R.layout.fragment_oil_avg) {

    private val oilAvgViewModel: OilAvgViewModel by viewModels()

    @Inject
    lateinit var dateConverter: DateConverterUtil
    private val oilAvgAdapter by lazy { OilAvgRecyclerAdapter(mutableListOf()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.oilKind.text = getString(R.string.gasoline)
        binding.oilAvgRecyclerView.adapter = oilAvgAdapter
        setupChart()
        observeViewModel()
        oilAvgViewModel.fetchOilAvg(getString(R.string.gasoline_code))
    }

    private fun setupChart() {
        binding.lineChart.apply {
            xAxis.valueFormatter =
                IndexAxisValueFormatter(dateConverter.getRecentWeekDays(requireContext()))
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            axisRight.isEnabled = false
            description.text = getString(R.string.recent_week_price)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                oilAvgViewModel.oilAvgList.collect { oilAvgPriceInfoList ->
                    updateChart(oilAvgPriceInfoList)
                    updateCurrentPrice(oilAvgPriceInfoList)
                    oilAvgAdapter.setItems(oilAvgPriceInfoList.reversed())
                }
            }
        }
    }

    private fun updateChart(oilAvgPriceInfoList: List<OilAveragePriceInfo>) {
        val entries = oilAvgPriceInfoList.mapIndexed { index, it ->
            Entry(index.toFloat(), it.oilPrice.toFloat())
        }
        val dataSet = LineDataSet(entries, getString(R.string.gas_station_price)).apply {
            color = Color.rgb(255, 153, 0)
            lineWidth = 2f
            setCircleColor(Color.rgb(253, 153, 0))
            circleRadius = 4f
            setDrawCircleHole(false)
        }
        binding.lineChart.data = LineData(dataSet)
        binding.lineChart.invalidate()
    }

    private fun updateCurrentPrice(oilAvgPriceInfoList: List<OilAveragePriceInfo>) {
        Log.e("TAG","updateCurrentPrice")
        if (oilAvgPriceInfoList.isNotEmpty()) {
            binding.priceText.text =
                RidRoundMath.roundStringToInteger(oilAvgPriceInfoList.last().oilPrice).toString()
        }
    }
}

