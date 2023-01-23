package org.techtown.find_gas_station.Retrofit.oilDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OIL
{

    @SerializedName("CVS_YN")
    @Expose
    private String CVS_YN;

    @SerializedName("GPOLL_DIV_CO")
    @Expose
    private String GPOLL_DIV_CO;

    @SerializedName("POLL_DIV_CO")
    @Expose
    private String POLL_DIV_CO;

    @SerializedName("LPG_YN")
    @Expose
    private String LPG_YN;

    @SerializedName("VAN_ADR")
    @Expose
    private String VAN_ADR;

    @SerializedName("NEW_ADR")
    @Expose
    private String NEW_ADR;

    @SerializedName("OIL_PRICE")
    @Expose
    private OIL_PRICE[] OIL_PRICE;

    @SerializedName("SIGUNCD")
    @Expose
    private String SIGUNCD;

    @SerializedName("MAINT_YN")
    @Expose
    private String MAINT_YN;

    @SerializedName("GIS_Y_COOR")
    @Expose
    private String GIS_Y_COOR;

    @SerializedName("CAR_WASH_YN")
    @Expose
    private String CAR_WASH_YN;

    @SerializedName("UNI_ID")
    @Expose
    private String UNI_ID;

    @SerializedName("OS_NM")
    @Expose
    private String OS_NM;

    @SerializedName("TEL")
    @Expose
    private String TEL;

    @SerializedName("PETRO_YN")
    @Expose
    private String KPETRO_YN;

    @SerializedName("GIS_X_COOR")
    @Expose
    private String GIS_X_COOR;

    public String getCVS_YN ()
    {
        return CVS_YN;
    }

    public void setCVS_YN (String CVS_YN)
    {
        this.CVS_YN = CVS_YN;
    }

    public String getGPOLL_DIV_CO ()
    {
        return GPOLL_DIV_CO;
    }

    public void setGPOLL_DIV_CO (String GPOLL_DIV_CO)
    {
        this.GPOLL_DIV_CO = GPOLL_DIV_CO;
    }

    public String getPOLL_DIV_CO ()
    {
        return POLL_DIV_CO;
    }

    public void setPOLL_DIV_CO (String POLL_DIV_CO)
    {
        this.POLL_DIV_CO = POLL_DIV_CO;
    }

    public String getLPG_YN ()
    {
        return LPG_YN;
    }

    public void setLPG_YN (String LPG_YN)
    {
        this.LPG_YN = LPG_YN;
    }

    public String getVAN_ADR ()
    {
        return VAN_ADR;
    }

    public void setVAN_ADR (String VAN_ADR)
    {
        this.VAN_ADR = VAN_ADR;
    }

    public String getNEW_ADR ()
    {
        return NEW_ADR;
    }

    public void setNEW_ADR (String NEW_ADR)
    {
        this.NEW_ADR = NEW_ADR;
    }

    public OIL_PRICE[] getOIL_PRICE ()
    {
        return OIL_PRICE;
    }

    public void setOIL_PRICE (OIL_PRICE[] OIL_PRICE)
    {
        this.OIL_PRICE = OIL_PRICE;
    }

    public String getSIGUNCD ()
    {
        return SIGUNCD;
    }

    public void setSIGUNCD (String SIGUNCD)
    {
        this.SIGUNCD = SIGUNCD;
    }

    public String getMAINT_YN ()
    {
        return MAINT_YN;
    }

    public void setMAINT_YN (String MAINT_YN)
    {
        this.MAINT_YN = MAINT_YN;
    }

    public String getGIS_Y_COOR ()
    {
        return GIS_Y_COOR;
    }

    public void setGIS_Y_COOR (String GIS_Y_COOR)
    {
        this.GIS_Y_COOR = GIS_Y_COOR;
    }

    public String getCAR_WASH_YN ()
    {
        return CAR_WASH_YN;
    }

    public void setCAR_WASH_YN (String CAR_WASH_YN)
    {
        this.CAR_WASH_YN = CAR_WASH_YN;
    }

    public String getUNI_ID ()
    {
        return UNI_ID;
    }

    public void setUNI_ID (String UNI_ID)
    {
        this.UNI_ID = UNI_ID;
    }

    public String getOS_NM ()
    {
        return OS_NM;
    }

    public void setOS_NM (String OS_NM)
    {
        this.OS_NM = OS_NM;
    }

    public String getTEL ()
    {
        return TEL;
    }

    public void setTEL (String TEL)
    {
        this.TEL = TEL;
    }

    public String getKPETRO_YN ()
    {
        return KPETRO_YN;
    }

    public void setKPETRO_YN (String KPETRO_YN)
    {
        this.KPETRO_YN = KPETRO_YN;
    }

    public String getGIS_X_COOR ()
    {
        return GIS_X_COOR;
    }

    public void setGIS_X_COOR (String GIS_X_COOR)
    {
        this.GIS_X_COOR = GIS_X_COOR;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [CVS_YN = "+CVS_YN+", GPOLL_DIV_CO = "+GPOLL_DIV_CO+", POLL_DIV_CO = "+POLL_DIV_CO+", LPG_YN = "+LPG_YN+", VAN_ADR = "+VAN_ADR+", NEW_ADR = "+NEW_ADR+", OIL_PRICE = "+OIL_PRICE+", SIGUNCD = "+SIGUNCD+", MAINT_YN = "+MAINT_YN+", GIS_Y_COOR = "+GIS_Y_COOR+", CAR_WASH_YN = "+CAR_WASH_YN+", UNI_ID = "+UNI_ID+", OS_NM = "+OS_NM+", TEL = "+TEL+", KPETRO_YN = "+KPETRO_YN+", GIS_X_COOR = "+GIS_X_COOR+"]";
    }
}