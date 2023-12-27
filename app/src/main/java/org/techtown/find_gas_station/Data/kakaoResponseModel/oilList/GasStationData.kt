package org.techtown.find_gas_station.Data.kakaoResponseModel.oilList

data class GasStationData(val result : Result)

data class Result(val oil : List<GasStation>)

data class GasStation(val uniId : String, val pollDivCd : String, val osNm: String,
                       val price : Int, val distance : Double , val gisX : Double , val gisY : Double)