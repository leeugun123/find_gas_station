package org.techtown.find_gas_station.Retrofit.oilAvg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.techtown.find_gas_station.Retrofit.oilDetail.RESULT;

public class OilAvg {

    @SerializedName("RESULT")
    @Expose
    private org.techtown.find_gas_station.Retrofit.oilAvg.RESULT result;

    public org.techtown.find_gas_station.Retrofit.oilAvg.RESULT getRESULT ()
    {
        return result;
    }

    public void setRESULT (org.techtown.find_gas_station.Retrofit.oilAvg.RESULT RESULT)
    {
        this.result = RESULT;
    }


    @Override
    public String toString()
    {
        return "ClassPojo [RESULT = "+result+"]";
    }

}
