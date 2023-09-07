package org.techtown.find_gas_station.Data.kakaoResponseModel.oilList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyPojo
{
    @SerializedName("RESULT")
    @Expose
    private org.techtown.find_gas_station.Data.kakaoResponseModel.oilList.RESULT RESULT;

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