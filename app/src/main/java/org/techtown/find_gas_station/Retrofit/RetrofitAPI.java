package org.techtown.find_gas_station.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitAPI {

    @GET
    Call<MyPojo> getOilList(@Query("code") String code,
                            @Query("out") String out,
                            @Query("x") String x,
                            @Query("y") String y,
                            @Query("radius") String radius,
                            @Query("prodcd") String prodcd,
                            @Query("sort") String sort);


}
