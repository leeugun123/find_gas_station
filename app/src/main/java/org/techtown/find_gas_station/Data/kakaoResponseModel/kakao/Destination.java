package org.techtown.find_gas_station.Data.kakaoResponseModel.kakao;

import com.google.gson.annotations.SerializedName;

public class Destination {

    @SerializedName("x")
    private double x;
    @SerializedName("y")
    private double y;

    @SerializedName("key")
    private String key;

    public Destination(double x, double y, String key) {
        this.x = x;
        this.y = y;
        this.key = key;
    }
}
