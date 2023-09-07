package org.techtown.find_gas_station.Data.kakaoResponseModel.oilAvg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OIL {


    @SerializedName("DATE")
    @Expose
    private String DATE;

    @SerializedName("PRODCD")
    @Expose
    private String PRODCD;

    @SerializedName("PRICE")
    @Expose
    private String PRICE;

    public String getDate(){
        return DATE;
    }

    public void SetDate(String DATE){
        this.DATE = DATE;
    }

    public String getProdcd(){
        return PRODCD;
    }

    public void setProdcd(String PRODCDR){
        this.PRODCD = PRODCD;
    }

    public String getPrice(){
        return PRICE;
    }

    public void setPrice(String PRICE){
        this.PRICE = PRICE;
    }

    public OIL(String date,String price){
        this.DATE = date;
        this.PRICE = price;
    }





}
