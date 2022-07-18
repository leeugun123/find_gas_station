package org.techtown.find_gas_station;

public class oil_list {

    String oil_name;
    String price;
    String distance;
    String oil_kind;
    int image;

    float wgsX;//wgs84 좌표 x
    float wgsY;//wgs84 좌표 y

    public oil_list(String oil_name, String price, String distance,String oil_kind,int image, float wgsX,float wgsY){
        this.oil_name = oil_name;
        this.price = price;
        this.distance = distance;
        this.oil_kind = oil_kind;
        this.image = image;
        this.wgsX = wgsX;
        this.wgsX = wgsY;

    }

    public String get_oil_name(){
        return oil_name;
    }

    public String getPrice(){
        return price;
    }

    public String getDistance(){
        return distance;
    }

    public String getOil_kind(){
        return oil_kind;
    }

    public int get_image(){
        return image;
    }

    public void setOil_name(String name){
        this.oil_name = name;
    }

    public void setPrice(String price){
        this.price = price;
    }

    public void setDistance(String distance){
        this.distance = distance;
    }

    public void setOil_kind(String oil_kind){this.oil_kind = oil_kind;}

    public void set_image(int image){
        this.image = image;
    }

    public void setWgs84_X(float latitude){ this.wgsX = wgsX; }

    public float getKatecX(){ return wgsX; }

    public void setKatecY(float wgsY){ this.wgsY = wgsY; }

    public float getKatecY(){ return wgsY; }




}
