package org.techtown.find_gas_station.Data.kakaoResponseModel.kakao;

import com.google.gson.annotations.SerializedName;

public class Destination {

    @SerializedName("key")
    private String key;

    @SerializedName("x")
    private Double x;

    @SerializedName("y")
    private Double y;

    public Destination(String key, Double x, Double y) {
        this.x = x;
        this.y = y;
        this.key = key;
    }


}
