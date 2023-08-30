package org.techtown.find_gas_station.Retrofit;

import org.techtown.find_gas_station.BuildConfig;
import org.techtown.find_gas_station.Retrofit.kakaoResponseModel.KakaoResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface Kakao_RetrofitApi {

    @GET("v1/future/directions")
    @Headers("Authorization: " + BuildConfig.KAKAO_API_KEY)
    Call<KakaoResponseModel> getDirections(
            @Query("origin") String origin,
            @Query("destination") String destination,
            @Query("departure_time") String departureTime
    );


}
