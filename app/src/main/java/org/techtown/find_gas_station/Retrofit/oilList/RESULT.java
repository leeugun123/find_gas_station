package org.techtown.find_gas_station.Retrofit.oilList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.techtown.find_gas_station.Retrofit.oilList.OIL;

public class RESULT
{
    @SerializedName("OIL")
    @Expose
    private org.techtown.find_gas_station.Retrofit.oilList.OIL[] OIL;

    public OIL[] getOIL ()
    {
        return OIL;
    }

    public void setOIL (OIL[] OIL)
    {
        this.OIL = OIL;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [OIL = "+OIL+"]";
    }
}
