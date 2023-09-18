package org.techtown.find_gas_station.MVVM;

import static org.techtown.find_gas_station.Fragment.HomeFragment.getWgsMyX;
import static org.techtown.find_gas_station.Fragment.HomeFragment.getWgsMyY;
import static org.techtown.find_gas_station.Fragment.HomeFragment.moil_list;

import android.annotation.SuppressLint;
import android.app.Application;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.maps.GoogleMap;

import org.techtown.find_gas_station.BuildConfig;
import org.techtown.find_gas_station.Comparator.OilRoadDistanceComparator;
import org.techtown.find_gas_station.Comparator.OilSpendTimeComparator;
import org.techtown.find_gas_station.Data.kakaoResponseModel.kakao.Destination;
import org.techtown.find_gas_station.Data.kakaoResponseModel.kakao.DirectionRequest;
import org.techtown.find_gas_station.Data.kakaoResponseModel.kakao.DirectionResponse;
import org.techtown.find_gas_station.Data.kakaoResponseModel.kakao.Origin;
import org.techtown.find_gas_station.Data.kakaoResponseModel.kakao.Route;
import org.techtown.find_gas_station.Data.kakaoResponseModel.oilAvg.OilPriceInfo;
import org.techtown.find_gas_station.Data.kakaoResponseModel.oilDetail.GasStationInfo;
import org.techtown.find_gas_station.Data.kakaoResponseModel.oilList.GasStationData;
import org.techtown.find_gas_station.Fragment.OilAvgRecyclerAdapter;
import org.techtown.find_gas_station.GPS.GeoTrans;
import org.techtown.find_gas_station.GPS.GeoTransPoint;
import org.techtown.find_gas_station.Fragment.HomeFragment;
import org.techtown.find_gas_station.MyRecyclerAdapter;
import org.techtown.find_gas_station.Comparator.OilDistanceComparator;
import org.techtown.find_gas_station.Comparator.OilPriceComparator;
import org.techtown.find_gas_station.OilList;
import org.techtown.find_gas_station.R;
import org.techtown.find_gas_station.Retrofit.Kakao_RetrofitApi;
import org.techtown.find_gas_station.Retrofit.Opinet_RetrofitApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetOilRepository {

    private Retrofit opinet_retrofit;
    private Retrofit kakao_retrofit;
    private final static String OPINET_BASE_URL = "http:///www.opinet.co.kr/";
    private final static String KAKAO_BASE_URL = "https://apis-navi.kakaomobility.com/";
    Opinet_RetrofitApi opinet_retrofitApi;
    Kakao_RetrofitApi kakao_retrofitApi;

    //--------------------- 레트로핏 변수 들
    private String opinet_apiKey = BuildConfig.GAS_API_KEY;
    //------------- 오피넷 API key
    private MyRecyclerAdapter myRecyclerAdapter;

    private String oil = "";
    private String priority = "";

    private ArrayList<OilList> plusOilList;


    public GetOilRepository(Application application){
        super();

        moil_list = new ArrayList<>();
        plusOilList = new ArrayList<>();
        //카카오api를 활용한 추가적인 정보가 필요할때 사용

        kakao_retrofit = new Retrofit.Builder()
                .baseUrl(KAKAO_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        opinet_retrofit = new Retrofit.Builder()
                .baseUrl(OPINET_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        opinet_retrofitApi = opinet_retrofit.create(Opinet_RetrofitApi.class);
        kakao_retrofitApi = kakao_retrofit.create(Kakao_RetrofitApi.class);


    }

    public void getOilList(
            RecyclerView mRecyclerView,
            GoogleMap mMap, ProgressBar progressBar, String strXpos, String strYpos, String radius, String sort, String oilKind) {

        oil = oilKind;

        opinet_retrofitApi.getOilList(opinet_apiKey, "json", strXpos, strYpos, radius,oilKind,sort)
                .enqueue(new Callback<GasStationData>() {

                    @Override
                    public void onResponse(Call<GasStationData> call, Response<GasStationData> response) {

                        moil_list = new ArrayList<>();
                        plusOilList = new ArrayList<>();
                        //oil_list 초기화

                        if(response.isSuccessful()){

                            GasStationData gasStationData = response.body();
                            GasStationData.Result result = gasStationData.getRESULT();

                            if(result.getOIL().size() == 0){

                                Log.e("TAG","데이터가 비었음");

                                myRecyclerAdapter = new MyRecyclerAdapter(moil_list,mMap,sort);
                                mRecyclerView.setAdapter(myRecyclerAdapter);
                                myRecyclerAdapter.notifyDataSetChanged();

                                progressBar.setVisibility(View.GONE);
                                HomeFragment.empty = true;

                                return;

                            }//데이터가 존재하지 않는 경우 예외처리


                            for(int i=0; i<result.getOIL().size(); i++){

                                if(i == 30)
                                    break;

                                String uid = result.getOIL().get(i).getUNI_ID();
                                //주유소 ID

                                String distance = String.valueOf(result.getOIL().get(i).getDISTANCE());
                                //주유소 거리

                                String name = result.getOIL().get(i).getOS_NM();
                                //주유소 이름(상호명)

                                String gas_price = Integer.toString(result.getOIL().get(i).getPRICE());
                                //주유소 가격

                                String inputOil;


                                if(oil.equals("B027")){
                                    inputOil = "휘발유";
                                }
                                else if(oil.equals("D047")){
                                    inputOil = "경유";
                                }
                                else if(oil.equals("B034")){
                                    inputOil = "고급휘발유";
                                }
                                else if(oil.equals("C004")){
                                    inputOil = "실내등유";
                                }
                                else
                                    inputOil = "자동차부탄";

                                float xPos = (float) result.getOIL().get(i).getGIS_X_COOR();
                                //x좌표 위치
                                float yPos = (float) result.getOIL().get(i).getGIS_Y_COOR();
                                //y좌표 위치

                                GeoTransPoint point = new GeoTransPoint(xPos,yPos);

                                GeoTransPoint out = GeoTrans.convert(GeoTrans.KATEC, GeoTrans.GEO,point);
                                //KATEC -> Wgs84좌표계로 변경

                                String trademark = result.getOIL().get(i).getPOLL_DIV_CD();
                                //트레이드 마크

                                int imageResource;
                                //이미지 리소스

                                if(trademark.equals("SKE")){
                                    imageResource = R.drawable.sk;
                                }
                                else if(trademark.equals("GSC")){
                                    imageResource = R.drawable.gs;
                                }
                                else if(trademark.equals("HDO")){
                                    imageResource = R.drawable.hdoil;
                                }
                                else if(trademark.equals("SOL")){
                                    imageResource = R.drawable.so;
                                }
                                else if(trademark.equals("RTO")){
                                    imageResource = R.drawable.rto;
                                }//비슷
                                else if(trademark.equals("RTX")){
                                    imageResource = R.drawable.rto;
                                }//비슷
                                else if(trademark.equals("RTX")){
                                    imageResource = R.drawable.rto;
                                }
                                else if(trademark.equals("NHO")){
                                    imageResource = R.drawable.nho;
                                }
                                else if(trademark.equals("E1G")){
                                    imageResource = R.drawable.e1;
                                }
                                else if(trademark.equals("SKG")){
                                    imageResource = R.drawable.skgas;
                                }
                                else
                                    imageResource = R.drawable.oil_2;

                                getOilDetail(sort, result.getOIL().size(), mRecyclerView,mMap, progressBar ,
                                        uid , name, gas_price, distance, inputOil,
                                        imageResource, (float)out.getX(), (float)out.getY());


                            }

                        }

                    }

                    @Override
                    public void onFailure(Call<GasStationData> call, Throwable t) {


                    }


                });


    }

    public void getOilDetail(String sort, int size, RecyclerView mRecyclerView,
                             GoogleMap mMap, ProgressBar progressBar , String uid, String name,
                             String gas_price, String distance, String inputOil,
                             int imageResource, float DestinationX, float DestinationY){


        opinet_retrofitApi.getOilDetail(opinet_apiKey,"json",uid)
                .enqueue(new Callback<GasStationInfo>() {

                        @Override
                        public void onResponse(Call<GasStationInfo> call, Response<GasStationInfo> response) {

                            if(response.isSuccessful()){

                                GasStationInfo gasDetail = response.body();
                                GasStationInfo.Result result = gasDetail.getRESULT();

                                //세차장, 편의점 정보
                                String carWash = result.getOIL().get(0).getCAR_WASH_YN();
                                String conStore = result.getOIL().get(0).getCVS_YN();

                                //상세 정보 받아오기
                                String lotNumberAddress = result.getOIL().get(0).getVAN_ADR();
                                String roadAddress = result.getOIL().get(0).getNEW_ADR();

                                String tel = result.getOIL().get(0).getTEL();
                                String sector = result.getOIL().get(0).getLPG_YN();

                                int dis = (int)Double.parseDouble(distance);
                                //소수점 짜르기

                                moil_list.add(new OilList(uid, name, gas_price, Integer.toString(dis),
                                        inputOil,imageResource, DestinationX, DestinationY,carWash,conStore,lotNumberAddress,roadAddress,
                                        tel,sector,"",""));

                                if(moil_list.size() == size || moil_list.size() == 30){

                                    if(sort.equals("3") || sort.equals("4")){

                                        getOilKakaoApi(progressBar ,mMap, mRecyclerView,sort);

                                        return;

                                    }//추가적인 카카오 api를 요구하는 경우


                                    if(sort.equals("1")){
                                        Collections.sort(moil_list,new OilPriceComparator());
                                    }//가격순
                                    else if(sort.equals("2")){
                                        Collections.sort(moil_list,new OilDistanceComparator());
                                    }//직경 거리순

                                    //불필요한 정렬 생성

                                    progressBar.setVisibility(View.GONE);
                                    myRecyclerAdapter = new MyRecyclerAdapter(moil_list,mMap,sort);
                                    mRecyclerView.setAdapter(myRecyclerAdapter);
                                    myRecyclerAdapter.notifyDataSetChanged();

                                }//데이터가 모두 도착 하면 실행

                            }


                        }

                        @Override
                        public void onFailure(Call<GasStationInfo> call, Throwable t) {


                        }


                });


    }

    //카카오 api는 wgs 좌표를 사용
    public void getOilKakaoApi(ProgressBar progressBar, GoogleMap mMap, RecyclerView mRecyclerView, String sort){


        HashMap<String , OilList> hashMap = new HashMap<>();
        //응답으로 섞인 OilList 객체들을 바로 잡기 위한 HashMap

        //여기까지 데이터는 옴.

        for(int i=0; i < moil_list.size(); i++){

            if(i == 30)
                break;

            hashMap.put(moil_list.get(i).getUid(), moil_list.get(i));

        }

        Destination[] destinations = new Destination[hashMap.size()];

        for(int i=0; i<hashMap.size(); i++){
            destinations[i] = new Destination( moil_list.get(i).getUid(), moil_list.get(i).getWgs84X(), moil_list.get(i).getWgs84Y());
        }

        if(sort.equals("3"))
            priority = "DISTANCE";
        else
            priority = "TIME";


        kakao_retrofitApi.getMultiDirections(new DirectionRequest(new Origin(Double.parseDouble(getWgsMyX) , Double.parseDouble(getWgsMyY)),
                        destinations,10000,priority))

                .enqueue(new Callback<DirectionResponse>() {
                    @Override
                    public void onResponse(Call<DirectionResponse> call, Response<DirectionResponse> response) {

                        if(response.isSuccessful()){

                            DirectionResponse multiRouteResponse = response.body();

                            Route[] routes = multiRouteResponse.getRoutes();

                            for(int i=0; i < hashMap.size(); i++){

                                String key = routes[i].getKey();
                                OilList oilList = hashMap.get(key);

                                String distance = Integer.toString(routes[i].getSummary().getDistance());
                                String spendTime = Integer.toString(routes[i].getSummary().getDuration());

                                oilList.setActDistance(distance);
                                oilList.setSpendTime(spendTime);

                                plusOilList.add(oilList);

                            }

                            if(priority.equals("TIME")){
                                Collections.sort(plusOilList , new OilSpendTimeComparator());
                            }else
                                Collections.sort(plusOilList , new OilRoadDistanceComparator());



                            progressBar.setVisibility(View.GONE);
                            myRecyclerAdapter = new MyRecyclerAdapter(plusOilList,mMap,sort);
                            mRecyclerView.setAdapter(myRecyclerAdapter);
                            myRecyclerAdapter.notifyDataSetChanged();


                        }
                        else{
                           int statusCode = response.code();
                        }


                    }

                    @Override
                    public void onFailure(Call<DirectionResponse> call, Throwable t) {

                    }



                });


    }
    //카카오 api 이용하여 추가정보(실제 거리 , 소요시간)를 가져옴.


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


    public String getCurrentDateTimeString() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        return dateFormat.format(new Date());

    }










}
