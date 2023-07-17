package org.techtown.find_gas_station.Retrofit.oilAvg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RESULT {

    @SerializedName("OIL")
    @Expose
    private org.techtown.find_gas_station.Retrofit.oilAvg.OIL[] oil;

    public org.techtown.find_gas_station.Retrofit.oilAvg.OIL[] getOil(){return oil;}

    public void setOil(org.techtown.find_gas_station.Retrofit.oilAvg.OIL[] oil){this.oil = oil; }

    @Override
    public String toString(){
        return "ClassPojo [OIL = "+ oil+"]";
    }
}
