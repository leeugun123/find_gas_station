package org.techtown.find_gas_station.Data.kakaoResponseModel.oilList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RESULT
{
    @SerializedName("OIL")
    @Expose
    private org.techtown.find_gas_station.Data.kakaoResponseModel.oilList.OIL[] OIL;

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
