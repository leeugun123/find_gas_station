package org.techtown.find_gas_station.Data.kakaoResponseModel.kakao;

import com.google.gson.annotations.SerializedName;

public class Origin {


    private Double x;

    private Double y;

    public Origin(Double x, Double y) {
        this.x = x;
        this.y = y;
    }
}
