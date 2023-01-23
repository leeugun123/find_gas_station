package org.techtown.find_gas_station.Retrofit.oilList;

import android.view.ViewDebug;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OIL
{
    @SerializedName("DISTANCE")
    @Expose
    private String DISTANCE;

    @SerializedName("UNI_ID")
    @Expose
    private String UNI_ID;

    @SerializedName("PRICE")
    @Expose
    private String PRICE;

    @SerializedName("OS_NM")
    @Expose
    private String OS_NM;

    @SerializedName("GIS_X_COOR")
    @Expose
    private String GIS_X_COOR;

    @SerializedName("POLL_DIV_CD")
    @Expose
    private String POLL_DIV_CD;

    @SerializedName("GIS_Y_COOR")
    @Expose
    private String GIS_Y_COOR;

    public String getDISTANCE ()
    {
        return DISTANCE;
    }

    public void setDISTANCE (String DISTANCE)
    {
        this.DISTANCE = DISTANCE;
    }

    public String getUNI_ID ()
    {
        return UNI_ID;
    }

    public void setUNI_ID (String UNI_ID)
    {
        this.UNI_ID = UNI_ID;
    }

    public String getPRICE ()
    {
        return PRICE;
    }

    public void setPRICE (String PRICE)
    {
        this.PRICE = PRICE;
    }

    public String getOS_NM ()
    {
        return OS_NM;
    }

    public void setOS_NM (String OS_NM)
    {
        this.OS_NM = OS_NM;
    }

    public String getGIS_X_COOR ()
    {
        return GIS_X_COOR;
    }

    public void setGIS_X_COOR (String GIS_X_COOR)
    {
        this.GIS_X_COOR = GIS_X_COOR;
    }

    public String getPOLL_DIV_CD ()
    {
        return POLL_DIV_CD;
    }

    public void setPOLL_DIV_CD (String POLL_DIV_CD)
    {
        this.POLL_DIV_CD = POLL_DIV_CD;
    }

    public String getGIS_Y_COOR ()
    {
        return GIS_Y_COOR;
    }

    public void setGIS_Y_COOR (String GIS_Y_COOR)
    {
        this.GIS_Y_COOR = GIS_Y_COOR;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [DISTANCE = "+DISTANCE+", UNI_ID = "+UNI_ID+", PRICE = "+PRICE+", OS_NM = "+OS_NM+", GIS_X_COOR = "+GIS_X_COOR+", POLL_DIV_CD = "+POLL_DIV_CD+", GIS_Y_COOR = "+GIS_Y_COOR+"]";
    }
}
