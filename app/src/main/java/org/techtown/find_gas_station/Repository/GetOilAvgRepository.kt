package org.techtown.find_gas_station.Repository

import android.app.Application
import androidx.lifecycle.MutableLiveData
import org.techtown.find_gas_station.Data.oilAvg.OilAveragePriceInfo
import org.techtown.find_gas_station.Data.oilAvg.OilAveragePriceInfoResult
import org.techtown.find_gas_station.Util.Api.ApiKey
import org.techtown.find_gas_station.Util.Api.Api_Instance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetOilAvgRepository(application: Application){

    private var oilAvgInfoLiveData : MutableLiveData<List<OilAveragePriceInfo>> = MutableLiveData()

    fun getOilAvgInfoLiveData() = this.oilAvgInfoLiveData

    fun getOilAvg(prodcd : String) {

        Api_Instance.opiRetrofitApi.getAvgRecentPrice(ApiKey.opiApiKey , "json", prodcd)

            .enqueue(object : Callback<OilAveragePriceInfoResult> {

                override fun onResponse(call: Call<OilAveragePriceInfoResult>, response: Response<OilAveragePriceInfoResult>) {

                    if (response.isSuccessful) {

                        val result = response.body()!!.oilAveragePriceInfoResult.oilAveragePriceInfo
                        oilAvgInfoLiveData.value = result

                    }


                }

                override fun onFailure(call: Call<OilAveragePriceInfoResult>, t: Throwable) {}

            })

    }




}