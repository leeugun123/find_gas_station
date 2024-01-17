package org.techtown.find_gas_station.Repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import org.techtown.find_gas_station.Data.OilList.GasStationInfoResult
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
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.CAR_BUTANE
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.CHECK_FOUR_SPEND_TIME
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.CHECK_PRICE_CONDITION
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.CHECK_THREE_ROAD_DISTANCE
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.CHECK_TWO_DIRECT_DISTANCE
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.GASOLINE_GUIDE
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.GASOLINE_GUIDE_ENGLISH
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.INDOOR_KEROSENE
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.INDOOR_KEROSENE_ENGLISH
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.PREMIUM_GASOLINE
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.PREMIUM_GASOLINE_ENGLISH
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.VIA_GUIDE
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

                        val gasStationData = response.body()
                        val result = gasStationData!!.oilInfoListResult.oilInfoList

                        for (i in result.indices) {

                            if (i == 30)
                                break

                            val uid = result[i].id
                            val distance = result[i].distance
                            val osName = result[i].osName
                            val gasPrice = result[i].price

                            val inputOil = getOilType(oilKind)

                            val xPos = result[i].gisX.toDouble()
                            val yPos = result[i].gisY.toDouble()
                            val out = GeoTrans.convert(GeoTrans.KATEC, GeoTrans.GEO, GeoTransPoint(xPos, yPos))
                            //KATEC -> Wgs84좌표계로 변경

                            val imageResource = getTrademarkImageResource(result[i].pollDivCd)

                            getOilDetail(sort, result.size, uid, osName, gasPrice, distance, inputOil, imageResource, out.x.toFloat(), out.y.toFloat())

                        }

                    }

                }

                override fun onFailure(call: Call<GasStationInfoResult?>, t: Throwable) {}

            })
    }


    private fun getOilDetail(sort: String, size : Int, uid : String, name: String, gasPrice: String, distance: String, inputOil: String,
        imageResource : Int, destinationX : Float, destinationY : Float) {


        opiRetrofitApi.getOilDetail(OPI_API_KEY, JSON_FORMAT, uid)
            .enqueue(object : Callback<GasStationDetailInfoResult> {
                override fun onResponse(call: Call<GasStationDetailInfoResult>, response: Response<GasStationDetailInfoResult>) {

                    if (response.isSuccessful) {

                        val oilDetailInfo = response.body()!!.gasStationDetailInfoResult.gasStationDetailInfo

                        val carWash = oilDetailInfo[0].carWashExist
                        val conStore = oilDetailInfo[0].conStoreExist
                        val lotNumberAddress = oilDetailInfo[0].address
                        val roadAddress =  oilDetailInfo[0].streetAddress
                        val tel = oilDetailInfo[0].calNumber
                        val sector = oilDetailInfo[0].sector
                        val dis = distance.toDouble().toInt()

                        tempList.add(TotalOilInfo(uid, name, gasPrice, dis.toString(), inputOil, imageResource, destinationX, destinationY,
                                carWash, conStore, lotNumberAddress, roadAddress, tel, sector, "", "" ))


                        if (tempList.size == size || tempList.size == 30) {

                            if (sort == CHECK_THREE_ROAD_DISTANCE || sort == CHECK_FOUR_SPEND_TIME) {
                                getOilKakaoApi(sort)
                                return
                            } //추가적인 카카오 api를 요구하는 경우
                            if (sort == CHECK_PRICE_CONDITION) {
                                Collections.sort(tempList, OilPriceComparator())
                            } //가격순
                            else if (sort == CHECK_TWO_DIRECT_DISTANCE) {
                                Collections.sort(tempList, OilDistanceComparator())
                            } //직경 거리순

                            oilListLiveData.value = tempList


                        }
                    //데이터가 모두 도착 하면 실행

                    }


                }

                override fun onFailure(call: Call<GasStationDetailInfoResult>, t: Throwable) {
                }

            })

    }


    //카카오 api는 wgs 좌표를 사용
    fun getOilKakaoApi(sort: String) {

        val destinations = arrayOfNulls<Destination>(tempList.size)

        for (i in tempList.indices) {
            val uid = tempList[i].uid
            val wgsX = tempList[i].wgs84X.toDouble()
            val wgsY = tempList[i].wgs84Y.toDouble()
            destinations[i] = Destination(uid, wgsX, wgsY)
        }

        kakaoRetrofitApi.getMultiDirections(
            DirectionRequest(Origin(
                    HomeFragment.getWgsMyX.toDouble(),
                    HomeFragment.getWgsMyY.toDouble()
                ),
                destinations, KAKAO_REQUEST_RADIUS
            )
        ).enqueue(object : Callback<DirectionResponse> {

                override fun onResponse(call: Call<DirectionResponse>, response: Response<DirectionResponse>) {

                    if (response.isSuccessful) {

                        val routes = response.body()!!.routes

                        for (i in tempList.indices) {

                            val oilList = tempList[i]
                            oilList.actDistance = routes[i].summary.distance
                            oilList.spendTime = routes[i].summary.duration
                            plusList.add(oilList)
                        }
                        if (sort == CHECK_FOUR_SPEND_TIME) {
                            Collections.sort(plusList, OilSpendTimeComparator())
                        } else
                            Collections.sort(plusList, OilRoadDistanceComparator())

                        oilListLiveData.value = plusList

                    }

                }

                override fun onFailure(call: Call<DirectionResponse?>, t: Throwable) {}
            })
    }
    //카카오 api 이용하여 추가정보(실제 거리 , 소요시간)를 가져옴.


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
            GASOLINE_GUIDE_ENGLISH -> GASOLINE_GUIDE
            VIA_GUIDE_ENGLISH -> VIA_GUIDE
            PREMIUM_GASOLINE_ENGLISH -> PREMIUM_GASOLINE
            INDOOR_KEROSENE_ENGLISH -> INDOOR_KEROSENE
            else -> CAR_BUTANE
        }

    private fun listClear() {
        tempList.clear()
        plusList.clear()
    }


}