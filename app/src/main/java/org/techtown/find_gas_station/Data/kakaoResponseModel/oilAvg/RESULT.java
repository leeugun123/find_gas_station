package org.techtown.find_gas_station.Data.kakaoResponseModel.oilAvg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RESULT {

    @SerializedName("OIL")
    @Expose
    private OIL[] oil;

    public OIL[] getOil(){return oil;}

    public void setOil(OIL[] oil){this.oil = oil; }

    @Override
    public String toString(){
        return "ClassPojo [OIL = "+ oil+"]";
    }
}
