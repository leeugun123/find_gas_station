package org.techtown.find_gas_station.Data.kakaoResponseModel.kakao;

import java.util.List;

public class MultiRouteResponse {

    private String trans_id;
    private List<Route> routes;

    public String getTransId() {
        return trans_id;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public class Route {
        private int result_code;
        private String result_msg;
        private String key;
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
        private int distance;
        private int duration;

        public int getDistance() {
            return distance;
        }

        public int getDuration() {
            return duration;
        }

    }

}
