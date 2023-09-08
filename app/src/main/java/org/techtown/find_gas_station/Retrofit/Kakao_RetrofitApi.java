package org.techtown.find_gas_station.Retrofit;

import org.techtown.find_gas_station.BuildConfig;
import org.techtown.find_gas_station.Data.kakaoResponseModel.kakao.Destination;
import org.techtown.find_gas_station.Data.kakaoResponseModel.kakao.MultiRouteResponse;
import org.techtown.find_gas_station.Data.kakaoResponseModel.kakao.OneRouteResponse;
import org.techtown.find_gas_station.Data.kakaoResponseModel.kakao.Origin;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
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




    @POST("v1/destinations/directions")
    @Headers("Authorization: KakaoAK " + BuildConfig.KAKAO_API_KEY)
    Call<MultiRouteResponse> getMultiDirections(
            @Query("origin") Origin origin,
            @Query("destinations") Destination[] destinations,
            @Query("radius") int radius,
            @Query("priority") String priority
    );
    //다중 목적지


}
