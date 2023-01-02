package org.techtown.find_gas_station.Retrofit;

import com.google.gson.annotations.SerializedName;

public class OilModel {

    //주유소코드
    @SerializedName("UNI_ID")
    private int id;

    //상표
    @SerializedName("POLL_DIV_CD")
    private String brand;

    //상호(주유소 이름)
    @SerializedName("OS_NM")
    private String name;

    //판매가격
    @SerializedName("PRICE")
    private int price;

    //거리
    @SerializedName("DISTANCE")
    private double distance;

    //GIS X 좌표
    @SerializedName("GIS_X_COOR")
    private double xPos;

    //GIS Y 좌표
    @SerializedName("GIX_Y_COOR")
    private double yPos;


}
