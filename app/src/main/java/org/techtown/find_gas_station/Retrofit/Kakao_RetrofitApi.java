package org.techtown.find_gas_station.Retrofit;

import org.techtown.find_gas_station.BuildConfig;
import org.techtown.find_gas_station.Data.kakaoResponseModel.Location;
import org.techtown.find_gas_station.Data.kakaoResponseModel.MultiRouteResponse;
import org.techtown.find_gas_station.Data.kakaoResponseModel.OneRouteResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface Kakao_RetrofitApi {

    @GET("v1/future/directions")
    @Headers("Authorization: KakaoAK " + BuildConfig.KAKAO_API_KEY)
    Call<OneRouteResponse> getOneDirections(
            @Query("origin") String origin,
            @Query("destination") String destination,
            @Query("departure_time") String departureTime
    );
    //단일 목적지


    @GET("v1/destinations/directions")
    @Headers("Authorization: KakaoAK " + BuildConfig.KAKAO_API_KEY)
    Call<MultiRouteResponse> getMultiDirections(
            @Query("origin")Location location,
            @Query("destinations") Location[] locations,
            @Query("radius") int radius
    );
    //다중 목적지


}
