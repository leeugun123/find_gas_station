package org.techtown.find_gas_station.Data.kakaoResponseModel;

import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("name")
    private String name;

    @SerializedName("x")
    private double x;
    @SerializedName("y")
    private double y;


    Location(String name,double x,double y){
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
