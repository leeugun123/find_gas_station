package org.techtown.find_gas_station.Repository

import android.app.Application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.techtown.find_gas_station.Data.OilList.GasStationInfoResult
import org.techtown.find_gas_station.Data.TotalOilInfo
import org.techtown.find_gas_station.Data.kakao.Request.Destination
import org.techtown.find_gas_station.Data.kakao.Request.DirectionRequest
import org.techtown.find_gas_station.Data.kakao.Request.Origin
import org.techtown.find_gas_station.Data.kakao.Response.DirectionResponse
import org.techtown.find_gas_station.Data.kakao.Response.Route
import org.techtown.find_gas_station.Data.oilDetail.GasStationDetailInfoResult
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.Util.Api.ApiKey.OPI_API_KEY
import org.techtown.find_gas_station.Util.Api.Api_Instance.kakaoRetrofitApi
import org.techtown.find_gas_station.Util.Api.Api_Instance.opiRetrofitApi
import org.techtown.find_gas_station.Util.Comparator.OilRoadDistanceComparator
import org.techtown.find_gas_station.Util.Comparator.OilSpendTimeComparator
import org.techtown.find_gas_station.Util.Constant.ConstantGuide.JSON_FORMAT
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.CAR_BUTANE_KOREAN
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.CHECK_FOUR_SPEND_TIME
import org.techtown.find_gas_station.Util.Constant.ConstantOilCondition.CHECK_THREE_ROAD_DISTANCE
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
import java.util.Collections

class GetOilRepository(application : Application) {


    private var tempList : MutableList<TotalOilInfo> = mutableListOf()


    fun getOilList() = tempList

    suspend fun requestOilList(xPos : String, yPos : String, radius : String, sort : String, oilKind : String) {

        listClear()

        val response = withContext(Dispatchers.IO) {
            opiRetrofitApi.getOilList(OPI_API_KEY, JSON_FORMAT, xPos, yPos, radius, oilKind, sort)
        }

        if (response.isSuccessful){
            val oilResponse = response.body()
            val size = oilResponse?.oilInfoListResult?.oilInfoList?.size
            apiSizeCheck(oilResponse, size!! , oilKind , sort)
        }

    }

    private suspend fun apiSizeCheck(oilResponse: GasStationInfoResult?, size : Int, oilKind: String, sort: String) {

        if(size > 0)
            handleOilListResponse(oilResponse, oilKind, sort)

    }// api 호출이 만료되면 빈 데이터가 들어옴. 따라서 만료되거나 점검하는지 체크하는 메소드


    private suspend fun handleOilListResponse(gasStationData: GasStationInfoResult? , oilKind : String , sort : String){

        val result = gasStationData?.let { adjustSize(it) }
        val inputOil = getOilType(oilKind)

        result?.forEach { oilInfo ->

            val out = GeoTrans.convert(GeoTrans.KATEC, GeoTrans.GEO, GeoTransPoint(oilInfo.gisX.toDouble(), oilInfo.gisY.toDouble()))
            getOilDetail(sort, result.size, oilInfo.id, oilInfo.osName, oilInfo.price, oilInfo.distance, inputOil,
                getTrademarkImageResource(oilInfo.pollDivCd), out.x.toFloat(), out.y.toFloat())
        }

    }

    private fun adjustSize(it : GasStationInfoResult) = if (30 < it.oilInfoListResult.oilInfoList.size)
        it.oilInfoListResult.oilInfoList.take(30)
    else
        it.oilInfoListResult.oilInfoList

    private suspend fun getOilDetail(sort: String, size : Int, uid : String, name: String, gasPrice: String, distance: String, inputOil: String,
        imageResource : Int, destinationX : Float, destinationY : Float) {

        val response = withContext(Dispatchers.IO) {
            opiRetrofitApi.getOilDetail(OPI_API_KEY, JSON_FORMAT, uid)
        }

        if (response.isSuccessful)
            handleOilDetailResponse(response.body(), sort, size, uid, name, gasPrice, distance, inputOil, imageResource, destinationX, destinationY)

    }

    private suspend fun handleOilDetailResponse(gasStationDetailInfo: GasStationDetailInfoResult?, sort: String, size: Int,
                                        uid: String, name: String, gasPrice: String, distance: String, inputOil: String,
                                        imageResource: Int, destinationX: Float, destinationY: Float) {
        gasStationDetailInfo?.let {

            val oilDetailInfo = it.gasStationDetailInfoResult.gasStationDetailInfo

            tempList.add(
                TotalOilInfo(
                    uid, name, gasPrice, distance.toDouble().toInt().toString(), inputOil, imageResource, destinationX,
                    destinationY, oilDetailInfo[0].carWashExist, oilDetailInfo[0].conStoreExist, oilDetailInfo[0].address,
                    oilDetailInfo[0].streetAddress, oilDetailInfo[0].calNumber, oilDetailInfo[0].sector, "", ""
                )
            )

            checkTempListSize(size, sort)
        }

    }

    private suspend fun checkTempListSize(size : Int , sort : String) {

        if ((tempList.size == size || tempList.size == 30) &&
            (sort == CHECK_THREE_ROAD_DISTANCE || sort == CHECK_FOUR_SPEND_TIME))
                getOilKakaoApi(sort)

    }


    //카카오 api는 wgs 좌표를 사용
    private suspend fun getOilKakaoApi(sort: String) {

        val destinations = arrayOfNulls<Destination>(tempList.size)
        destinationsProcessing(destinations)

        val response = withContext(Dispatchers.IO) {
            kakaoRetrofitApi.getMultiDirections(DirectionRequest(Origin(HomeFragment.getWgsMyX.toDouble(), HomeFragment.getWgsMyY.toDouble()),
                destinations, KAKAO_REQUEST_RADIUS))
        }

        if (response.isSuccessful)
            handleKakaoApiResponse(response.body(),sort)

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
            insertPlusList(it.routes)
            checkRoadOrSpend(sort)
        }

    }

    private fun insertPlusList(routes : List<Route>){
        for (i in tempList.indices) {
            tempList[i].actDistance = routes[i].summary.distance
            tempList[i].spendTime = routes[i].summary.duration
        }
    }


    private fun checkRoadOrSpend(sort : String) {

        if (sort == CHECK_FOUR_SPEND_TIME)
            Collections.sort(tempList, OilSpendTimeComparator())
         else
            Collections.sort(tempList, OilRoadDistanceComparator())

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
    }


}