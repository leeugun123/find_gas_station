package org.techtown.find_gas_station.Retrofit.oilDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RESULT {

    @SerializedName("OIL")
    @Expose
    private OIL[] OIL;

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
