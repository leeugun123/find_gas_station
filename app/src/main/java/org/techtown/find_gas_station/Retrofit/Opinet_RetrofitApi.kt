package org.techtown.find_gas_station.Retrofit

import org.techtown.find_gas_station.Data.OilList.GasStationInfoResult
import org.techtown.find_gas_station.Data.oilAvg.OilAveragePriceInfoResult
import org.techtown.find_gas_station.Data.oilDetail.GasStationDetailInfoResult
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Opinet_RetrofitApi {
    @GET("api/aroundAll.do")
    suspend fun getOilList(
        @Query("code") code : String,
        @Query("out") out : String,
        @Query("x") x : String,
        @Query("y") y : String,
        @Query("radius") radius : String,
        @Query("prodcd") prodcd : String,
        @Query("sort") sort : String
    ) : Response<GasStationInfoResult>

    @GET("api/detailById.do")
    suspend fun getOilDetail(
        @Query("code") code : String,
        @Query("out") out : String,
        @Query("id") id : String
    ) : Response<GasStationDetailInfoResult>


    @GET("api/avgRecentPrice.do")
    suspend fun getAvgRecentPrice(@Query("code") code: String,
                          @Query("out") out: String,
                          @Query("prodcd") prodcd: String
    ) : Response<OilAveragePriceInfoResult>

}