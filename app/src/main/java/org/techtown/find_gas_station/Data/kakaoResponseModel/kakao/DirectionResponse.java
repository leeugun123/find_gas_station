package org.techtown.find_gas_station.Data.kakaoResponseModel.kakao;

import com.google.gson.annotations.SerializedName;

public class DirectionResponse {


    private String trans_id;


    private Route[] routes;

    public String getTrans_id() {
        return trans_id;
    }

    public void setTrans_id(String trans_id) {
        this.trans_id = trans_id;
    }

    public Route[] getRoutes() {
        return routes;
    }

    public void setRoutes(Route[] routes) {
        this.routes = routes;
    }
}

