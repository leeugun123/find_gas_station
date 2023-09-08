package org.techtown.find_gas_station.Data.kakaoResponseModel.kakao;

import com.google.gson.annotations.SerializedName;

public class Destination {

    @SerializedName("key")
    private String key;

    @SerializedName("x")
    private double x;
    @SerializedName("y")
    private double y;

    public Destination(String key, double x, double y){

        this.key = key;
        this.x = x;
        this.y = y;

    }

    public String getKey(){
        return this.key;
    }

    public double getX(){
        return this.x;
    }

    public double getY(){
        return this.y;
    }

}
