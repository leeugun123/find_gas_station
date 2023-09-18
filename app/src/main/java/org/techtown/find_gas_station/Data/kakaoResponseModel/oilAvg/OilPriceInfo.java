package org.techtown.find_gas_station.Data.kakaoResponseModel.oilAvg;

import java.util.List;

public class OilPriceInfo {
    private Result RESULT;

    public Result getRESULT() {
        return RESULT;
    }

    public void setRESULT(Result RESULT) {
        this.RESULT = RESULT;
    }

    public static class Result {
        private List<OilPrice> OIL;

        public List<OilPrice> getOIL() {
            return OIL;
        }

        public void setOIL(List<OilPrice> OIL) {
            this.OIL = OIL;
        }
    }

    public static class OilPrice {
        private String DATE;
        private String PRODCD = "";
        private double PRICE;

        public OilPrice(String date, double price){
          this.DATE = date;
          this.PRICE = price;
        }

        public String getDATE() {
            return DATE;
        }

        public void setDATE(String DATE) {
            this.DATE = DATE;
        }

        public String getPRODCD() {
            return PRODCD;
        }

        public void setPRODCD(String PRODCD) {
            this.PRODCD = PRODCD;
        }

        public double getPRICE() {
            return PRICE;
        }

        public void setPRICE(double PRICE) {
            this.PRICE = PRICE;
        }
    }
}
