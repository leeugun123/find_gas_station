package org.techtown.find_gas_station.Retrofit.oilDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OilDetail
{
    @SerializedName("RESULT")
    @Expose
    private RESULT RESULT;

    public RESULT getRESULT ()
    {
        return RESULT;
    }

    public void setRESULT (RESULT RESULT)
    {
        this.RESULT = RESULT;
    }


    @Override
    public String toString()
    {
        return "ClassPojo [RESULT = "+RESULT+"]";
    }

}

