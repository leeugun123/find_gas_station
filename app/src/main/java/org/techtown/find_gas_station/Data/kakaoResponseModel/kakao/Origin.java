package org.techtown.find_gas_station.Data.kakaoResponseModel.kakao;

import com.google.gson.annotations.SerializedName;

public class Origin {

    @SerializedName("name")
    private String name;

    @SerializedName("x")
    private double x;
    @SerializedName("y")
    private double y;


    public Origin(String name,double x,double y){
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public String getName(){
        return name;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }


}
