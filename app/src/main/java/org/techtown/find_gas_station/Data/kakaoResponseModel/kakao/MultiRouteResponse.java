package org.techtown.find_gas_station.Data.kakaoResponseModel.kakao;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MultiRouteResponse {

    @SerializedName("trans_id")
    private String trans_id;
    @SerializedName("routes")
    private Route[] routes;

    public String getTransId() {
        return trans_id;
    }

    public Route[] getRoutes() {
        return routes;
    }

    public class Route {

        @SerializedName("result_code")
        private int result_code;

        @SerializedName("result_msg")
        private String result_msg;

        @SerializedName("key")
        private String key;

        @SerializedName("summary")
        private Summary summary;

        public int getResultCode() {
            return result_code;
        }

        public String getResultMsg() {
            return result_msg;
        }

        public String getKey() {
            return key;
        }

        public Summary getSummary() {
            return summary;
        }

    }


    public class Summary {

        @SerializedName("distance")
        private int distance;

        @SerializedName("duration")
        private int duration;

        public int getDistance() {
            return distance;
        }

        public int getDuration() {
            return duration;
        }

    }

}
