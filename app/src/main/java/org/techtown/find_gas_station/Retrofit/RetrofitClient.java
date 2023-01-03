package org.techtown.find_gas_station.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static RetrofitClient instance = null;
    //private static RetrofitAPI retrofitAPI;

    private final static String BASE_UIRL = "http:///www.opinet.co.kr/api/aroundAll.do/";

    //싱글톤에 맞추어 변수들을 전부 static으로 만들어줌, 메소드 역시

    private RetrofitClient(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_UIRL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //retrofitApI = retrofit.create(RetrofitAPI.class);

    }

    public static RetrofitClient getInstance(){

        if(instance == null){
            instance = new RetrofitClient();
        }

        return instance;
    }

    /*
    public static RetrofitApi getRetrofitAPI(){
        return retrofitAPI;
    }
    */

}
