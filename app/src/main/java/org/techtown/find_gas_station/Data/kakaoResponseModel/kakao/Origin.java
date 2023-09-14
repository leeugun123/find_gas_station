package org.techtown.find_gas_station.Data.kakaoResponseModel.kakao;

import com.google.gson.annotations.SerializedName;

public class Origin {

    @SerializedName("x")
    private double x;

    @SerializedName("y")
    private double y;

    public Origin(double x, double y) {
        this.x = x;
        this.y = y;
    }

}
