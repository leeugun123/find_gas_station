package org.techtown.find_gas_station.MVVM;

import static org.techtown.find_gas_station.Fragment.HomeFragment.getWgsMyX;
import static org.techtown.find_gas_station.Fragment.HomeFragment.getWgsMyY;
import static org.techtown.find_gas_station.Fragment.HomeFragment.moil_list;

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
import org.techtown.find_gas_station.Data.kakaoResponseModel.kakao.Destination;
import org.techtown.find_gas_station.Data.kakaoResponseModel.kakao.DirectionRequest;
import org.techtown.find_gas_station.Data.kakaoResponseModel.kakao.DirectionResponse;
import org.techtown.find_gas_station.Data.kakaoResponseModel.kakao.Origin;
import org.techtown.find_gas_station.Data.kakaoResponseModel.kakao.Route;
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
import org.techtown.find_gas_station.Data.kakaoResponseModel.oilAvg.OIL;
import org.techtown.find_gas_station.Data.kakaoResponseModel.oilAvg.OilAvg;
import org.techtown.find_gas_station.Data.kakaoResponseModel.oilDetail.OilDetail;
import org.techtown.find_gas_station.Data.kakaoResponseModel.oilList.MyPojo;
import org.techtown.find_gas_station.Data.kakaoResponseModel.oilList.RESULT;

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

    private String opinet_apiKey = BuildConfig.GAS_API_KEY;

    private MyRecyclerAdapter myRecyclerAdapter;

    String oil = "";

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

    public static String doubleToInt(String price){

        String temp = "";

        for(int i=0; i<price.length(); i++){

            if(price.charAt(i) == '.')
                break;

            temp += price.charAt(i);

        }

        return temp;
    }
    //가격에 소수점 제거 메소드

    public void getOilList(
            RecyclerView mRecyclerView,
            GoogleMap mMap, ProgressBar progressBar, String strXpos, String strYpos, String radius, String sort, String oilKind) {

        oil = oilKind;

        opinet_retrofitApi.getOilList(opinet_apiKey, "json", strXpos, strYpos, radius,oilKind,sort)
                .enqueue(new Callback<MyPojo>() {

                    @Override
                    public void onResponse(Call<MyPojo> call, Response<MyPojo> response) {

                        moil_list = new ArrayList<>();
                        plusOilList = new ArrayList<>();
                        //oil_list 초기화

                        if(response.isSuccessful()){

                            MyPojo myPojo = response.body();
                            RESULT result = myPojo.getRESULT();

                            if(result.getOIL().length == 0){

                                Log.e("TAG","데이터가 비었음");

                                myRecyclerAdapter = new MyRecyclerAdapter(moil_list,mMap,sort);
                                mRecyclerView.setAdapter(myRecyclerAdapter);
                                myRecyclerAdapter.notifyDataSetChanged();


                                progressBar.setVisibility(View.GONE);
                                HomeFragment.empty = true;

                                return;

                            }//데이터가 존재하지 않는 경우 예외처리



                            for(int i=0; i<result.getOIL().length; i++){

                                String uid = result.getOIL()[i].getUNI_ID();
                                //주유소 ID

                                String distance = result.getOIL()[i].getDISTANCE();
                                //주유소 거리

                                String name = result.getOIL()[i].getOS_NM();
                                //주유소 이름(상호명)

                                String gas_price = result.getOIL()[i].getPRICE();
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


                                float xPos = Float.parseFloat(result.getOIL()[i].getGIS_X_COOR());
                                //x좌표 위치

                                float yPos = Float.parseFloat(result.getOIL()[i].getGIS_Y_COOR());
                                //y좌표 위치

                                GeoTransPoint point = new GeoTransPoint(xPos,yPos);

                                GeoTransPoint out = GeoTrans.convert(GeoTrans.KATEC, GeoTrans.GEO,point);
                                //KATEC -> Wgs84좌표계로 변경

                                String trademark = result.getOIL()[i].getPOLL_DIV_CD();
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

                                getOilDetail(sort, result.getOIL().length, mRecyclerView,mMap, progressBar ,
                                        uid , name, gas_price, distance, inputOil,
                                        imageResource, (float)out.getX(), (float)out.getY());


                            }

                        }

                    }

                    @Override
                    public void onFailure(Call<MyPojo> call, Throwable t) {


                    }


                });


    }

    public void getOilDetail(String sort, int size, RecyclerView mRecyclerView,
                             GoogleMap mMap, ProgressBar progressBar , String uid, String name,
                             String gas_price, String distance, String inputOil,
                             int imageResource, float DestinationX, float DestinationY){


        opinet_retrofitApi.getOilDetail(opinet_apiKey,"json",uid)
                .enqueue(new Callback<OilDetail>() {

                        @Override
                        public void onResponse(Call<OilDetail> call, Response<OilDetail> response) {

                            if(response.isSuccessful()){

                                OilDetail oilDetail = response.body();
                                org.techtown.find_gas_station.Data.kakaoResponseModel.oilDetail.RESULT result = oilDetail.getRESULT();

                                //세차장, 편의점 정보
                                String carWash = result.getOIL()[0].getCAR_WASH_YN();
                                String conStore = result.getOIL()[0].getCVS_YN();

                                //상세 정보 받아오기
                                String lotNumberAddress = result.getOIL()[0].getVAN_ADR();
                                String roadAddress = result.getOIL()[0].getNEW_ADR();

                                String tel = result.getOIL()[0].getTEL();
                                String sector = result.getOIL()[0].getLPG_YN();

                                int dis = (int)Double.parseDouble(distance);
                                //소수점 짜르기

                                moil_list.add(new OilList(uid, name, gas_price, Integer.toString(dis),
                                        inputOil,imageResource, DestinationX, DestinationY,carWash,conStore,lotNumberAddress,roadAddress,
                                        tel,sector,"",""));

                                if(moil_list.size() == size){

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
                        public void onFailure(Call<OilDetail> call, Throwable t) {


                        }


                });


    }

    //카카오 api는 wgs 좌표를 사용
    public void getOilKakaoApi(ProgressBar progressBar, GoogleMap mMap, RecyclerView mRecyclerView, String sort){

        Destination[] destinations = new Destination[moil_list.size()];
        HashMap<String , OilList> hashMap = new HashMap<>();
        //응답으로 섞인 OilList 객체들을 바로 잡기 위한 HashMap

        //여기까지 데이터는 옴.

        for(int i=0; i < destinations.length; i++){

            String oilUid = moil_list.get(i).getUid();
            double x = moil_list.get(i).getWgs84X();
            double y = moil_list.get(i).getWgs84Y();

            destinations[i] = new Destination(oilUid, x , y);

            hashMap.put(oilUid, moil_list.get(i));



        }


        String priority = "";

        if(sort.equals("3"))
            priority = "DISTANCE";
        else
            priority = "TIME";


        kakao_retrofitApi.getMultiDirections(new DirectionRequest(new Origin(Double.parseDouble("127.13144306487084") , Double.parseDouble("37.44134209110179")),
                        destinations,5000, priority))

                .enqueue(new Callback<DirectionResponse>() {
                    @Override
                    public void onResponse(Call<DirectionResponse> call, Response<DirectionResponse> response) {


                        if(response.isSuccessful()){

                            DirectionResponse multiRouteResponse = response.body();

                            Route[] routes = multiRouteResponse.getRoutes();

                            for(int i=0; i<routes.length; i++){

                                String key = routes[i].getKey();
                                OilList oilList = hashMap.get(key);

                                String distance = Integer.toString(routes[i].getSummary().getDistance());
                                String spendTime = Integer.toString(routes[i].getSummary().getDuration());

                                Log.e("TAG",distance + "카카오 api");

                                oilList.setActDistance(distance);
                                oilList.setSpendTime(spendTime);

                                plusOilList.add(oilList);

                            }

                            progressBar.setVisibility(View.GONE);
                            myRecyclerAdapter = new MyRecyclerAdapter(plusOilList,mMap,sort);
                            mRecyclerView.setAdapter(myRecyclerAdapter);
                            myRecyclerAdapter.notifyDataSetChanged();


                        }
                        else{

                           int statusCode = response.code();
                           Log.e("TAG",statusCode + "");

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
                .enqueue(new Callback<OilAvg>() {

                    @Override
                    public void onResponse(Call<OilAvg> call, Response<OilAvg> response) {

                        if(response.isSuccessful()){

                            OilAvg oilAvg = response.body();
                            org.techtown.find_gas_station.Data.kakaoResponseModel.oilAvg.RESULT result = oilAvg.getRESULT();

                            List<Entry> entries = new ArrayList<>();
                            ArrayList<OIL> Avg = new ArrayList<>();

                            for(int i=0; i<result.getOil().length; i++){

                                OIL oil = result.getOil()[i];

                                //recyclerView에 담을 ArrayList<Oil>
                                Avg.add(new OIL(
                                        oil.getDate(),
                                        doubleToInt(oil.getPrice())
                                ));

                                entries.add(new Entry(i,
                                                     Integer.parseInt(doubleToInt(oil.getPrice()))));
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
                                priceText.setText(Avg.get(0).getPrice());

                            oilAvg_recyclerView.setAdapter(new OilAvgRecyclerAdapter(Avg));

                        }


                    }

                    @Override
                    public void onFailure(Call<OilAvg> call, Throwable t) {



                    }



                });


    }


    public String getCurrentDateTimeString() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        return dateFormat.format(new Date());

    }










}
