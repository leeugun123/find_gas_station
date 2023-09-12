package org.techtown.find_gas_station.Data.kakaoResponseModel.kakao;

import com.google.gson.annotations.SerializedName;

public class Summary {

    @SerializedName("distance")
    private int distance;

    @SerializedName("duration")
    private int duration;

    public int  getDistance(){
        return distance;
    }

    public int getDuration(){
        return duration;
    }
}
