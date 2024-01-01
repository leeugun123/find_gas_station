package org.techtown.find_gas_station.Repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import org.techtown.find_gas_station.BuildConfig
import org.techtown.find_gas_station.Data.OilList.GasStationInfoResult
import org.techtown.find_gas_station.Data.TotalOilInfo
import org.techtown.find_gas_station.Data.kakao.Request.Destination
import org.techtown.find_gas_station.Data.kakao.Request.DirectionRequest
import org.techtown.find_gas_station.Data.kakao.Request.Origin
import org.techtown.find_gas_station.Data.kakao.Response.DirectionResponse
import org.techtown.find_gas_station.Data.oilDetail.GasStationDetailInfoResult
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.Util.ApiKey.opiApiKey
import org.techtown.find_gas_station.Util.Api_Instance.kakaoRetrofitApi
import org.techtown.find_gas_station.Util.Api_Instance.opiRetrofitApi
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


    //------------- 오피넷 API key


    private var oilListLiveData : MutableLiveData<List<TotalOilInfo>> = MutableLiveData()
    private var tempList : MutableList<TotalOilInfo> = mutableListOf()
    private var plusList : MutableList<TotalOilInfo> = mutableListOf()

    fun getOilListLiveData() = this.oilListLiveData

    fun searchOilList(xPos : String, yPos : String, radius : String, sort : String, oilKind : String) {

        opiRetrofitApi.getOilList(opiApiKey, "json", xPos, yPos, radius, oilKind, sort)
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


        opiRetrofitApi.getOilDetail(opiApiKey, "json", uid)
            .enqueue(object : Callback<GasStationDetailInfoResult> {
                override fun onResponse(call: Call<GasStationDetailInfoResult>, response: Response<GasStationDetailInfoResult>) {

                    if (response.isSuccessful) {

                        val oilDetailInfo = response.body()!!.gasStationDetailInfoResult.gasStationDetailInfo

                        val carWash = oilDetailInfo.carWashExist
                        val conStore = oilDetailInfo.conStoreExist
                        val lotNumberAddress = oilDetailInfo.address
                        val roadAddress =  oilDetailInfo.streetAddress
                        val tel = oilDetailInfo.calNumber
                        val sector = oilDetailInfo.sector
                        val dis = distance.toDouble().toInt()

                        tempList.add(TotalOilInfo(uid, name, gasPrice, dis.toString(), inputOil, imageResource, destinationX, destinationY,
                                carWash, conStore, lotNumberAddress, roadAddress, tel, sector, "", "" ))


                        if (tempList.size == size || tempList.size == 30) {

                            if (sort == "3" || sort == "4") {
                                getOilKakaoApi(sort)
                                return
                            } //추가적인 카카오 api를 요구하는 경우
                            if (sort == "1") {
                                Collections.sort(tempList, OilPriceComparator())
                            } //가격순
                            else if (sort == "2") {
                                Collections.sort(tempList, OilDistanceComparator())
                            } //직경 거리순

                            oilListLiveData.setValue(tempList)


                        }
                    //데이터가 모두 도착 하면 실행

                    }

                }

                override fun onFailure(call: Call<GasStationDetailInfoResult>, t: Throwable) {}

            })

    }


    //카카오 api는 wgs 좌표를 사용
    fun getOilKakaoApi(sort: String) {

        val destinations = arrayListOf<Destination>()

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

                        oilListLiveData.setValue(plusList)

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


    /*
    public void getOilAvg(LineChart lineChart, RecyclerView oilAvg_recyclerView, TextView priceText, String prodcd){

        opinet_retrofitApi.getAvgRecentPrice(opinet_apiKey,"json",prodcd)
                .enqueue(new Callback<OilPriceInfo>() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(Call<OilPriceInfo> call, Response<OilPriceInfo> response) {

                        if(response.isSuccessful()){

                            OilPriceInfo oilPriceInfo = response.body();
                            OilPriceInfo.Result result = oilPriceInfo.getRESULT();

                            List<Entry> entries = new ArrayList<>();
                            ArrayList<OilPriceInfo.OilPrice> Avg = new ArrayList<>();

                            for(int i=0; i<result.getOIL().size(); i++){

                                OilPriceInfo.OilPrice oilPrice = result.getOIL().get(i);

                                //recyclerView에 담을 ArrayList<Oil>
                                Avg.add(new OilPriceInfo.OilPrice(
                                        oilPrice.getDATE(),
                                        (int) oilPrice.getPRICE()
                                ));

                                entries.add(new Entry(i, (int) oilPrice.getPRICE()));
                            }

                            LineDataSet dataSet = new LineDataSet(entries, "주유소 가격");
                            dataSet.setColor(Color.rgb(255,153,000));
                            dataSet.setLineWidth(2f);
                            dataSet.setCircleColor(Color.rgb(255,153,000));
                            dataSet.setCircleRadius(4f);
                            dataSet.setDrawCircleHole(false);

                            // Example: Customize the x-axis labels
                            XAxis xAxis = lineChart.getXAxis();
                            xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{"7일전", "6일전", "5일전", "4일전", "3일전",
                            "2일전","1일전"}));

                            List<ILineDataSet> dataSets = new ArrayList<>();
                            dataSets.add(dataSet);

                            LineData lineData = new LineData(dataSets);

                            lineChart.setData(lineData);
                            lineChart.getDescription().setText("최근 일주일 전국 유가 가격");
                            lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                            lineChart.getAxisRight().setEnabled(false);
                            lineChart.invalidate();

                            Collections.reverse(Avg);
                            //역순 뒤집기

                            if(Avg.size() > 0)
                                priceText.setText(Integer.toString( (int) Avg.get(0).getPRICE()));

                            oilAvg_recyclerView.setAdapter(new OilAvgRecyclerAdapter(Avg));

                        }


                    }

                    @Override
                    public void onFailure(Call<OilPriceInfo> call, Throwable t) {



                    }



                });


    }
    */




}