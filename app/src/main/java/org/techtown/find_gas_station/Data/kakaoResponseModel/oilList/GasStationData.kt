package org.techtown.find_gas_station.Data.kakaoResponseModel.oilList;
import java.util.List;

public class GasStationData {
    private Result RESULT;

    public Result getRESULT() {
        return RESULT;
    }

    public void setRESULT(Result RESULT) {
        this.RESULT = RESULT;
    }

    public static class Result {
        private List<GasStation> OIL;

        public List<GasStation> getOIL() {
            return OIL;
        }

        public void setOIL(List<GasStation> OIL) {
            this.OIL = OIL;
        }

    }

    public static class GasStation {
        private String UNI_ID;
        private String POLL_DIV_CD;
        private String OS_NM;
        private int PRICE;
        private double DISTANCE;
        private double GIS_X_COOR;
        private double GIS_Y_COOR;

        public String getUNI_ID() {
            return UNI_ID;
        }

        public void setUNI_ID(String UNI_ID) {
            this.UNI_ID = UNI_ID;
        }

        public String getPOLL_DIV_CD() {
            return POLL_DIV_CD;
        }

        public void setPOLL_DIV_CD(String POLL_DIV_CD) {
            this.POLL_DIV_CD = POLL_DIV_CD;
        }

        public String getOS_NM() {
            return OS_NM;
        }

        public void setOS_NM(String OS_NM) {
            this.OS_NM = OS_NM;
        }

        public int getPRICE() {
            return PRICE;
        }

        public void setPRICE(int PRICE) {
            this.PRICE = PRICE;
        }

        public double getDISTANCE() {
            return DISTANCE;
        }

        public void setDISTANCE(double DISTANCE) {
            this.DISTANCE = DISTANCE;
        }

        public double getGIS_X_COOR() {
            return GIS_X_COOR;
        }

        public void setGIS_X_COOR(double GIS_X_COOR) {
            this.GIS_X_COOR = GIS_X_COOR;
        }

        public double getGIS_Y_COOR() {
            return GIS_Y_COOR;
        }

        public void setGIS_Y_COOR(double GIS_Y_COOR) {
            this.GIS_Y_COOR = GIS_Y_COOR;
        }

    }

}

