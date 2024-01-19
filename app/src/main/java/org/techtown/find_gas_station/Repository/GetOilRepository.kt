package org.techtown.find_gas_station.Repository

import android.app.Application
import androidx.lifecycle.MutableLiveData
import org.techtown.find_gas_station.Data.OilList.GasStationInfoResult
import org.techtown.find_gas_station.Data.OilList.Result
import org.techtown.find_gas_station.Data.OilList.StationInfo
import org.techtown.find_gas_station.Data.TotalOilInfo
import org.techtown.find_gas_station.Data.kakao.Request.Destination
import org.techtown.find_gas_station.Data.kakao.Request.DirectionRequest
import org.techtown.find_gas_station.Data.kakao.Request.Origin
import org.techtown.find_gas_station.Data.kakao.Response.DirectionResponse
import org.techtown.find_gas_station.Data.oilDetail.GasStationDetailInfoResult
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.Util.Api.ApiKey.OPI_API_KEY
import org.techtown.find_gas_station.Util.Api.Api_Instance.kakaoRetrofitApi
import org.techtown.find_gas_station.Util.Api.Api_Instance.opiRetrofitApi
import org.techtown.find_gas_station.Util.Comparator.OilDistanceComparator
import org.techtown.find_gas_station.Util.Comparator.OilPriceComparator
import org.techtown.find_gas_station.Util.Comparator.OilRoadDistanceComparator
import org.techtown.find_gas_station.Util.Comparator.OilSpendTimeComparator
import org.techtown.find_gas_station.Util.Constant.ConstantGuide.JSON_FORMAT
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.CAR_BUTANE_KOREAN
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.CHECK_FOUR_SPEND_TIME
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.CHECK_PRICE_CONDITION
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.CHECK_THREE_ROAD_DISTANCE
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.CHECK_TWO_DIRECT_DISTANCE
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.GASOLINE_KOREAN
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.GASOLINE_GUIDE_ENGLISH
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.INDOOR_KEROSENE_KOREAN
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.INDOOR_KEROSENE_ENGLISH
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.PREMIUM_GASOLINE_KOREAN
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.PREMIUM_GASOLINE_ENGLISH
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.VIA_KOREAN
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.VIA_GUIDE_ENGLISH
import org.techtown.find_gas_station.Util.Constant.ConstantsTime.KAKAO_REQUEST_RADIUS
import org.techtown.find_gas_station.Util.GPS.GeoTrans
import org.techtown.find_gas_station.Util.GPS.GeoTransPoint
import org.techtown.find_gas_station.View.Fragment.HomeFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Collections

class GetOilRepository(application : Application) {

    private var oilListLiveData : MutableLiveData<List<TotalOilInfo>> = MutableLiveData()
    private var tempList : MutableList<TotalOilInfo> = mutableListOf()
    private var plusList : MutableList<TotalOilInfo> = mutableListOf()

    fun getOilListLiveData() = this.oilListLiveData

    fun searchOilList(xPos : String, yPos : String, radius : String, sort : String, oilKind : String) {
        listClear()

        opiRetrofitApi.getOilList(OPI_API_KEY, JSON_FORMAT, xPos, yPos, radius, oilKind, sort)
            .enqueue(object : Callback<GasStationInfoResult> {
                override fun onResponse(call: Call<GasStationInfoResult>, response: Response<GasStationInfoResult>) {
                    if (response.isSuccessful) {
                        handleOilListResponse(response.body(), oilKind ,sort)
                    }
                }
                override fun onFailure(call: Call<GasStationInfoResult?>, t: Throwable) {}
            })
    }

    private fun handleOilListResponse(gasStationData: GasStationInfoResult? , oilKind : String , sort : String){

        gasStationData?.let {

            val result = adjustSize(it)
            val inputOil = getOilType(oilKind)

            result.forEach { oilInfo ->

                val out = GeoTrans.convert(GeoTrans.KATEC, GeoTrans.GEO, GeoTransPoint(oilInfo.gisX.toDouble(), oilInfo.gisY.toDouble()))
                getOilDetail(sort, result.size, oilInfo.id , oilInfo.osName, oilInfo.price, oilInfo.distance, inputOil,
                    getTrademarkImageResource(oilInfo.pollDivCd), out.x.toFloat(), out.y.toFloat())

            }
        }
    }

    private fun adjustSize(it : GasStationInfoResult) = if (30 < it.oilInfoListResult.oilInfoList.size)
        it.oilInfoListResult.oilInfoList.take(30)
    else
        it.oilInfoListResult.oilInfoList

    private fun getOilDetail(sort: String, size : Int, uid : String, name: String, gasPrice: String, distance: String, inputOil: String,
        imageResource : Int, destinationX : Float, destinationY : Float) {

        opiRetrofitApi.getOilDetail(OPI_API_KEY, JSON_FORMAT, uid)
            .enqueue(object : Callback<GasStationDetailInfoResult> {

                override fun onResponse(call: Call<GasStationDetailInfoResult>, response: Response<GasStationDetailInfoResult>) {
                    if (response.isSuccessful)
                        handleOilDetailResponse(response.body(), sort, size, uid, name, gasPrice, distance, inputOil, imageResource, destinationX, destinationY)

                }
                override fun onFailure(call: Call<GasStationDetailInfoResult>, t: Throwable) {}

            })
    }

    private fun handleOilDetailResponse(gasStationDetailInfo: GasStationDetailInfoResult?, sort: String, size: Int,
                                        uid: String, name: String, gasPrice: String, distance: String, inputOil: String,
                                        imageResource: Int, destinationX: Float, destinationY: Float) {
        gasStationDetailInfo?.let {

            val oilDetailInfo = it.gasStationDetailInfoResult.gasStationDetailInfo

            tempList.add(TotalOilInfo(uid, name, gasPrice, distance.toDouble().toInt().toString(), inputOil, imageResource, destinationX, destinationY,
                oilDetailInfo[0].carWashExist, oilDetailInfo[0].conStoreExist, oilDetailInfo[0].address, oilDetailInfo[0].streetAddress,
                oilDetailInfo[0].calNumber, oilDetailInfo[0].sector, "", ""))

            checkTempListSize(size,sort)

        }

    }

    private fun checkTempListSize(size : Int , sort : String) {

        if (tempList.size == size || tempList.size == 30) {

            if (sort == CHECK_THREE_ROAD_DISTANCE || sort == CHECK_FOUR_SPEND_TIME) {
                getOilKakaoApi(sort)
                return
            }
            if (sort == CHECK_PRICE_CONDITION) {
                Collections.sort(tempList, OilPriceComparator())
            } else if (sort == CHECK_TWO_DIRECT_DISTANCE) {
                Collections.sort(tempList, OilDistanceComparator())
            }

            oilListLiveData.value = tempList

        }

    }


    //카카오 api는 wgs 좌표를 사용
    private fun getOilKakaoApi(sort: String) {

        val destinations = arrayOfNulls<Destination>(tempList.size)

        destinationsProcessing(destinations)

        kakaoRetrofitApi.getMultiDirections(DirectionRequest(Origin(HomeFragment.getWgsMyX.toDouble(), HomeFragment.getWgsMyY.toDouble()),
                destinations, KAKAO_REQUEST_RADIUS)).enqueue(object : Callback<DirectionResponse> {

                override fun onResponse(call: Call<DirectionResponse>, response: Response<DirectionResponse>) {
                    if (response.isSuccessful)
                        handleKakaoApiResponse(response.body() , sort)
                }

                override fun onFailure(call: Call<DirectionResponse?>, t: Throwable) {}
            }
        )
    }

    private fun destinationsProcessing(destinations : Array<Destination?>) {

        for (i in tempList.indices) {
            val uid = tempList[i].uid
            val wgsX = tempList[i].wgs84X.toDouble()
            val wgsY = tempList[i].wgs84Y.toDouble()
            destinations[i] = Destination(uid, wgsX, wgsY)
        }

    }

    private fun handleKakaoApiResponse(directionResponse: DirectionResponse?, sort: String) {
        directionResponse?.let {
            val routes = it.routes

            for (i in tempList.indices) {
                val oilList = tempList[i]
                oilList.actDistance = routes[i].summary.distance
                oilList.spendTime = routes[i].summary.duration
                plusList.add(oilList)
            }

            if (sort == CHECK_FOUR_SPEND_TIME) {
                Collections.sort(plusList, OilSpendTimeComparator())
            } else {
                Collections.sort(plusList, OilRoadDistanceComparator())
            }

            oilListLiveData.value = plusList

        }

    }



            private fun getTrademarkImageResource(trademark: String) =
        when (trademark) {
            "SKE" -> R.drawable.sk
            "GSC" -> R.drawable.gs
            "HDO" -> R.drawable.hdoil
            "SOL" -> R.drawable.so
            "RTO", "RTX" -> R.drawable.rto
            "NHO" -> R.drawable.nho
            "E1G" -> R.drawable.e1
            "SKG" -> R.drawable.skgas
            else -> R.drawable.oil_2
        }



    private fun getOilType(oilKind: String) =
        when (oilKind) {
            GASOLINE_GUIDE_ENGLISH -> GASOLINE_KOREAN
            VIA_GUIDE_ENGLISH -> VIA_KOREAN
            PREMIUM_GASOLINE_ENGLISH -> PREMIUM_GASOLINE_KOREAN
            INDOOR_KEROSENE_ENGLISH -> INDOOR_KEROSENE_KOREAN
            else -> CAR_BUTANE_KOREAN
        }

    private fun listClear() {
        tempList.clear()
        plusList.clear()
    }


}