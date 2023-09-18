package org.techtown.find_gas_station.Data.kakaoResponseModel.oilDetail;
import java.util.List;

public class GasStationInfo {
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
        private String POLL_DIV_CO;
        private String GPOLL_DIV_CO;
        private String OS_NM;
        private String VAN_ADR;
        private String NEW_ADR;
        private String TEL;
        private String SIGUNCD;
        private String LPG_YN;
        private String MAINT_YN;
        private String CAR_WASH_YN;
        private String KPETRO_YN;
        private String CVS_YN;
        private double GIS_X_COOR;
        private double GIS_Y_COOR;
        private List<OilPrice> OIL_PRICE;

        public String getUNI_ID() {
            return UNI_ID;
        }

        public void setUNI_ID(String UNI_ID) {
            this.UNI_ID = UNI_ID;
        }

        public String getPOLL_DIV_CO() {
            return POLL_DIV_CO;
        }

        public void setPOLL_DIV_CO(String POLL_DIV_CO) {
            this.POLL_DIV_CO = POLL_DIV_CO;
        }

        public String getGPOLL_DIV_CO() {
            return GPOLL_DIV_CO;
        }

        public void setGPOLL_DIV_CO(String GPOLL_DIV_CO) {
            this.GPOLL_DIV_CO = GPOLL_DIV_CO;
        }

        public String getOS_NM() {
            return OS_NM;
        }

        public void setOS_NM(String OS_NM) {
            this.OS_NM = OS_NM;
        }

        public String getVAN_ADR() {
            return VAN_ADR;
        }

        public void setVAN_ADR(String VAN_ADR) {
            this.VAN_ADR = VAN_ADR;
        }

        public String getNEW_ADR() {
            return NEW_ADR;
        }

        public void setNEW_ADR(String NEW_ADR) {
            this.NEW_ADR = NEW_ADR;
        }

        public String getTEL() {
            return TEL;
        }

        public void setTEL(String TEL) {
            this.TEL = TEL;
        }

        public String getSIGUNCD() {
            return SIGUNCD;
        }

        public void setSIGUNCD(String SIGUNCD) {
            this.SIGUNCD = SIGUNCD;
        }

        public String getLPG_YN() {
            return LPG_YN;
        }

        public void setLPG_YN(String LPG_YN) {
            this.LPG_YN = LPG_YN;
        }

        public String getMAINT_YN() {
            return MAINT_YN;
        }

        public void setMAINT_YN(String MAINT_YN) {
            this.MAINT_YN = MAINT_YN;
        }

        public String getCAR_WASH_YN() {
            return CAR_WASH_YN;
        }

        public void setCAR_WASH_YN(String CAR_WASH_YN) {
            this.CAR_WASH_YN = CAR_WASH_YN;
        }

        public String getKPETRO_YN() {
            return KPETRO_YN;
        }

        public void setKPETRO_YN(String KPETRO_YN) {
            this.KPETRO_YN = KPETRO_YN;
        }

        public String getCVS_YN() {
            return CVS_YN;
        }

        public void setCVS_YN(String CVS_YN) {
            this.CVS_YN = CVS_YN;
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

        public List<OilPrice> getOIL_PRICE() {
            return OIL_PRICE;
        }

        public void setOIL_PRICE(List<OilPrice> OIL_PRICE) {
            this.OIL_PRICE = OIL_PRICE;
        }
    }

    public static class OilPrice {
        private String PRODCD;
        private int PRICE;
        private String TRADE_DT;
        private String TRADE_TM;

        public String getPRODCD() {
            return PRODCD;
        }

        public void setPRODCD(String PRODCD) {
            this.PRODCD = PRODCD;
        }

        public int getPRICE() {
            return PRICE;
        }

        public void setPRICE(int PRICE) {
            this.PRICE = PRICE;
        }

        public String getTRADE_DT() {
            return TRADE_DT;
        }

        public void setTRADE_DT(String TRADE_DT) {
            this.TRADE_DT = TRADE_DT;
        }

        public String getTRADE_TM() {
            return TRADE_TM;
        }

        public void setTRADE_TM(String TRADE_TM) {
            this.TRADE_TM = TRADE_TM;
        }

    }

}
