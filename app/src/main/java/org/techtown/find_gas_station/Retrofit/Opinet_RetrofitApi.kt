package org.techtown.find_gas_station.Retrofit

import org.techtown.find_gas_station.Data.kakaoResponseModel.OilList.GasStationInfoResult
import org.techtown.find_gas_station.Data.kakaoResponseModel.oilAvg.OilPriceInfo
import org.techtown.find_gas_station.Data.kakaoResponseModel.oilDetail.GasStationDetailInfoResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Opinet_RetrofitApi {
    @GET("api/aroundAll.do")
    fun getOilList(
        @Query("code") code : String,
        @Query("out") out : String,
        @Query("x") x : String,
        @Query("y") y : String,
        @Query("radius") radius : String,
        @Query("prodcd") prodcd : String,
        @Query("sort") sort : String
    ): Call<GasStationInfoResult>

    @GET("api/detailById.do")
    fun getOilDetail(
        @Query("code") code : String,
        @Query("out") out : String,
        @Query("id") id : String
    ): Call<GasStationDetailInfoResult>

    @GET("api/avgRecentPrice.do")
    fun getAvgRecentPrice(@Query("code") code: String,
                          @Query("out") out: String,
                          @Query("prodcd") prodcd: String
    ): Call<OilPriceInfo>

}