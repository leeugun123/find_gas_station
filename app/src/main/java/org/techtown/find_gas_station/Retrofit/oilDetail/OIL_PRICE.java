package org.techtown.find_gas_station.Retrofit.oilDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OIL_PRICE
{
    @SerializedName("TRADE_DT")
    @Expose
    private String TRADE_DT;

    @SerializedName("PRODCD")
    @Expose
    private String PRODCD;

    @SerializedName("PRICE")
    @Expose
    private String PRICE;

    @SerializedName("TRADE_TM")
    @Expose
    private String TRADE_TM;


    public String getTRADE_DT ()
    {
        return TRADE_DT;
    }

    public void setTRADE_DT (String TRADE_DT)
    {
        this.TRADE_DT = TRADE_DT;
    }

    public String getPRODCD ()
    {
        return PRODCD;
    }

    public void setPRODCD (String PRODCD)
    {
        this.PRODCD = PRODCD;
    }

    public String getPRICE ()
    {
        return PRICE;
    }

    public void setPRICE (String PRICE)
    {
        this.PRICE = PRICE;
    }

    public String getTRADE_TM ()
    {
        return TRADE_TM;
    }

    public void setTRADE_TM (String TRADE_TM)
    {
        this.TRADE_TM = TRADE_TM;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [TRADE_DT = "+TRADE_DT+", PRODCD = "+PRODCD+", PRICE = "+PRICE+", TRADE_TM = "+TRADE_TM+"]";
    }
}