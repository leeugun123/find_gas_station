package org.techtown.find_gas_station.Data.kakaoResponseModel.oilAvg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OilAvg {

    @SerializedName("RESULT")
    @Expose
    private RESULT result;

    public RESULT getRESULT() {
        return result;
    }

    public void setRESULT (RESULT RESULT) {
        this.result = RESULT;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [RESULT = "+result+"]";
    }

}
