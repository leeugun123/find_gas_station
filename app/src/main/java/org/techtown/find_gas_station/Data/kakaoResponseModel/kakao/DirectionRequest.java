package org.techtown.find_gas_station.Data.kakaoResponseModel.kakao;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DirectionRequest {
    @SerializedName("origin")
    private Origin origin;

    @SerializedName("destinations")
    private Destination[] destinations;

    @SerializedName("radius")
    private int radius;

    @SerializedName("priority")
    private String priority;

    public DirectionRequest(Origin origin, Destination[] destinations, int radius , String priority) {
        this.origin = origin;
        this.destinations = destinations;
        this.radius = radius;
        this.priority = priority;
    }

}

