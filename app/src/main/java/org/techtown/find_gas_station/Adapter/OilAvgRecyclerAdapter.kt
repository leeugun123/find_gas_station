package org.techtown.find_gas_station.Adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.techtown.find_gas_station.Data.oilAvg.OilAveragePriceInfo
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.Util.UnitConverter.RidRoundMath.roundStringToInteger
import org.techtown.find_gas_station.databinding.OilavgBinding
import java.text.ParseException
import java.text.SimpleDateFormat

class OilAvgRecyclerAdapter(private val oilAvgList: List<OilAveragePriceInfo>) : RecyclerView.Adapter<OilAvgRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(val binding : OilavgBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                    = ViewHolder(OilavgBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun getItemCount() = oilAvgList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val oilAvg = oilAvgList[position]

        holder.binding.day.text = convertDateString(oilAvg.date)
        holder.binding.price.text = roundStringToInteger(oilAvg.oilPrice).toString()

        if (position == 0) {
            holder.binding.priceGap.text = "-"
            holder.binding.priceGap.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.gray))
        } else {

            val gap = priceGap(position)
            val priceText = gap.toString()

            with(holder.binding.priceGap) {
                text = priceText
                setTextColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        when {
                            gap > 0 -> R.color.orange
                            gap < 0 -> R.color.purple_700
                            else -> R.color.gray
                        }
                    )
                )
            }
        }



    }


    private fun priceGap(pos: Int) = roundStringToInteger(oilAvgList[pos].oilPrice) - roundStringToInteger(oilAvgList[pos - 1].oilPrice)

    @SuppressLint("SimpleDateFormat")
    private fun convertDateString(inputDate: String?): String {

        val inputFormat = SimpleDateFormat("yyyyMMdd")
        val outputFormat = SimpleDateFormat("MM월 dd일")

        try {
            val date = inputFormat.parse(inputDate)
            return outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return ""

    }





}