package org.techtown.find_gas_station.Retrofit;

import org.techtown.find_gas_station.BuildConfig;
import org.techtown.find_gas_station.Data.kakaoResponseModel.kakao.DirectionRequest;
import org.techtown.find_gas_station.Data.kakaoResponseModel.kakao.DirectionResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Kakao_RetrofitApi {

    @Headers({
            "Content-Type: application/json",
            "Authorization: KakaoAK 087efa22b8874111e44b386ebc24d525"
    })
    @POST("v1/destinations/directions")
    Call<DirectionResponse> getMultiDirections(@Body DirectionRequest request);
    //다중 목적지



}
