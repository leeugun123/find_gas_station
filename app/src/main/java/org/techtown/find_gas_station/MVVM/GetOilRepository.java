package org.techtown.find_gas_station.MVVM;

import android.app.Application;

import org.techtown.find_gas_station.GPS.GeoTrans;
import org.techtown.find_gas_station.GPS.GeoTransPoint;
import org.techtown.find_gas_station.R;
import org.techtown.find_gas_station.Retrofit.MyPojo;
import org.techtown.find_gas_station.Retrofit.RESULT;
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

    public GetOilRepository(Application application){
        super();

        moil_list = new ArrayList<>();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitAPI = retrofit.create(RetrofitAPI.class);

    }

    public List<oil_list> getOil(String APIkey,String xPos,String yPos,String radius,String sort,String oilKind) {
        retrofitAPI.getOilList(APIkey, "json", xPos, yPos, radius, sort, oilKind)
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

                                String oilKind = "";

                                if(result.getOIL()[i].getPOLL_DIV_CD().equals("B027")){
                                    oilKind = "휘발유";
                                }
                                else if(result.getOIL()[i].getPOLL_DIV_CD().equals("D047")){
                                    oilKind = "경유";
                                }else if(result.getOIL()[i].getPOLL_DIV_CD().equals("B034")){
                                    oilKind = "고급휘발유";
                                }
                                else if(result.getOIL()[i].getPOLL_DIV_CD().equals("C004")){
                                    oilKind = "실내등유";
                                }
                                else
                                    oilKind = "자동차부탄";


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


                                moil_list.add(new oil_list(uid,name,gas_price,distance+"m",
                                        oilKind,imageResource,(float)out.getX(),(float)out.getY()));

                            }


                        }

                    }

                    @Override
                    public void onFailure(Call<MyPojo> call, Throwable t) {


                    }


                });

        return moil_list;

    }


}
