package org.techtown.find_gas_station.MVVM;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.GoogleMap;

import org.techtown.find_gas_station.GPS.GeoTrans;
import org.techtown.find_gas_station.GPS.GeoTransPoint;
import org.techtown.find_gas_station.MyRecyclerAdapter;
import org.techtown.find_gas_station.R;
import org.techtown.find_gas_station.Retrofit.oilDetail.OilDetail;
import org.techtown.find_gas_station.Retrofit.oilList.MyPojo;
import org.techtown.find_gas_station.Retrofit.oilList.RESULT;
import org.techtown.find_gas_station.Retrofit.RetrofitAPI;
import org.techtown.find_gas_station.oil_list;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetOilRepository {

    private Retrofit retrofit;
    private final static String BASE_URL = "http:///www.opinet.co.kr/";
    RetrofitAPI retrofitAPI;
    List<oil_list> moil_list;

    private String apiKey = "F211129251";

    private MyRecyclerAdapter myRecyclerAdapter;

    String oil = "";

    public GetOilRepository(Application application){
        super();

        moil_list = new ArrayList<>();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitAPI = retrofit.create(RetrofitAPI.class);

    }

    public void getOilDetail(String uid){

        retrofitAPI.getOilDetail(apiKey,"json",uid)
                .enqueue(new Callback<OilDetail>() {
                    @Override
                    public void onResponse(Call<OilDetail> call, Response<OilDetail> response) {

                        if(response.isSuccessful()){

                            OilDetail oilDetail = response.body();
                            org.techtown.find_gas_station.Retrofit.oilDetail.RESULT result = oilDetail.getRESULT();



                        }



                    }

                    @Override
                    public void onFailure(Call<OilDetail> call, Throwable t) {

                    }
                });


    }

    public void getOilList(
                       RecyclerView mRecyclerView,
                       GoogleMap mMap,  String xPos, String yPos, String radius, String sort, String oilKind) {


        oil = oilKind;

        retrofitAPI.getOilList(apiKey, "json", xPos, yPos, radius,oilKind,sort)
                .enqueue(new Callback<MyPojo>() {

                    @Override
                    public void onResponse(Call<MyPojo> call, Response<MyPojo> response) {

                        moil_list.clear();

                        if(response.isSuccessful()){

                            MyPojo myPojo = response.body();
                            RESULT result = myPojo.getRESULT();

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

                               Log.e("TAG",uid);


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



                                moil_list.add(new oil_list(uid,name,gas_price, changeKm(distance)+"km",
                                        inputOil,imageResource,(float)out.getX(),(float)out.getY()));



                            }

                            myRecyclerAdapter = new MyRecyclerAdapter(moil_list,mMap);

                            mRecyclerView.setAdapter(myRecyclerAdapter);

                }

    }

    @Override
    public void onFailure(Call<MyPojo> call, Throwable t) {


                    }


                });



    }

    public static String changeKm(String distance){

        double doubleD = Double.parseDouble(distance)/1000;


        return String.format("%.1f",doubleD);

    }//m -> km 변경 메소드




}
