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
import org.techtown.find_gas_station.Util.GPS.GeoTrans
import org.techtown.find_gas_station.Util.GPS.GeoTransPoint
import org.techtown.find_gas_station.View.Fragment.HomeFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Collections

class GetOilRepository(application : Application) {

    private var oilListLiveData : MutableLiveData<List<TotalOilInfo>>
    private var tempList : MutableList<TotalOilInfo>
    private var plusList : MutableList<TotalOilInfo>

    init {
        oilListLiveData = MutableLiveData()
        tempList = mutableListOf()
        plusList = mutableListOf()
    }

    fun getOilListLiveData() = this.oilListLiveData

    fun searchOilList(xPos : String, yPos : String, radius : String, sort : String, oilKind : String) {

        listClear()

        opiRetrofitApi.getOilList(OPI_API_KEY, "json", xPos, yPos, radius, oilKind, sort)
            .enqueue(object : Callback<GasStationInfoResult> {

                override fun onResponse(call: Call<GasStationInfoResult>, response: Response<GasStationInfoResult>) {


                    Log.e("TAG","searchOilList")

                    if (response.isSuccessful) {

                        val gasStationData = response.body()
                        val result = gasStationData!!.oilInfoListResult.oilInfoList

                        for (i in result.indices) {

                            if (i == 30)
                                break

                            Log.e("TAG",result[i].id)

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


        opiRetrofitApi.getOilDetail(OPI_API_KEY, "json", uid)
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

                            Log.e("TAG", "sort" + sort)

                            if (sort == "3" || sort == "4") {
                                Log.e("TAG","카카오 api 요구")
                                getOilKakaoApi(sort)
                                return
                            } //추가적인 카카오 api를 요구하는 경우
                            if (sort == "1") {
                                Collections.sort(tempList, OilPriceComparator())
                            } //가격순
                            else if (sort == "2") {
                                Collections.sort(tempList, OilDistanceComparator())
                            } //직경 거리순

                            oilListLiveData.value = tempList


                        }
                    //데이터가 모두 도착 하면 실행

                    }
                    else {
                        Log.e("TAG","oilDetail 실패")
                    }

                }

                override fun onFailure(call: Call<GasStationDetailInfoResult>, t: Throwable) {
                    Log.e("TAG",t.message.toString())

                }

            })

    }


    //카카오 api는 wgs 좌표를 사용
    fun getOilKakaoApi(sort: String) {

        val destinations = arrayOfNulls<Destination>(tempList.size)

        for (i in tempList.indices) {
            val uid = tempList[i].getUid()
            val wgsX = tempList[i].getWgs84X().toDouble()
            val wgsY = tempList[i].getWgs84Y().toDouble()
            destinations[i] = Destination(uid, wgsX, wgsY)
        }

        kakaoRetrofitApi.getMultiDirections(
            DirectionRequest(Origin(
                    HomeFragment.getWgsMyX.toDouble(),
                    HomeFragment.getWgsMyY.toDouble()
                ),
                destinations, 10000
            )
        ).enqueue(object : Callback<DirectionResponse> {

                override fun onResponse(call: Call<DirectionResponse>, response: Response<DirectionResponse>) {

                    if (response.isSuccessful) {

                        val routes = response.body()!!.routes

                        for (i in tempList.indices) {

                            val oilList = tempList[i]
                            oilList.setActDistance(routes[i].summary.distance)
                            oilList.setSpendTime(routes[i].summary.duration)
                            plusList.add(oilList)
                        }
                        if (sort == "4") {
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
            "B027" -> "휘발유"
            "D047" -> "경유"
            "B034" -> "고급휘발유"
            "C004" -> "실내등유"
            else -> "자동차부탄"
        }

    private fun listClear() {
        tempList.clear()
        plusList.clear()
    }


}