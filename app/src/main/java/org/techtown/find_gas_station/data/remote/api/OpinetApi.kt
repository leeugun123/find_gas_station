package org.techtown.find_gas_station.data.remote.api

import org.techtown.find_gas_station.data.oilList.GasStationInfoResult
import org.techtown.find_gas_station.domain.areaprice.model.OilAveragePriceInfoResult
import org.techtown.find_gas_station.domain.station.model.oilDetail.GasStationDetailInfoResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpinetApi {
    @GET("api/aroundAll.do")
    suspend fun getStationList(
        @Query("code") code : String,
        @Query("out") out : String,
        @Query("x") x : String,
        @Query("y") y : String,
        @Query("radius") radius : String,
        @Query("prodcd") prodcd : String,
        @Query("sort") sort : String
    ) : Response<GasStationInfoResult>

    @GET("api/detailById.do")
    suspend fun getStationDetail(
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