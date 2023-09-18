package org.techtown.find_gas_station.Retrofit;

import org.techtown.find_gas_station.Data.kakaoResponseModel.oilAvg.OilAvg;
import org.techtown.find_gas_station.Data.kakaoResponseModel.oilDetail.OilDetail;
import org.techtown.find_gas_station.Data.kakaoResponseModel.oilList.GasStationData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Opinet_RetrofitApi {

    @GET("api/aroundAll.do")
    Call<GasStationData> getOilList(@Query("code") String code,
                                    @Query("out") String out,
                                    @Query("x") String x,
                                    @Query("y") String y,
                                    @Query("radius") String radius,
                                    @Query("prodcd") String prodcd,
                                    @Query("sort") String sort);


    @GET("api/detailById.do")
    Call<OilDetail> getOilDetail(@Query("code") String code, @Query("out") String out, @Query("id") String id);

    @GET("api/avgRecentPrice.do")
    Call<OilAvg> getAvgRecentPrice(@Query("code") String code, @Query("out") String out,
                                   @Query("prodcd") String prodcd);


}
