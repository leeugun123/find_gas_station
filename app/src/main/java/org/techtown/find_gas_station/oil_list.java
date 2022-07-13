package org.techtown.find_gas_station;

public class oil_list {

    String oil_name;
    String price;
    String distance;
    String oil_kind;
    int image;

    float katecX;//카텍 좌표 x
    float katecY;//카텍 좌표 y

    public oil_list(String oil_name, String price, String distance,String oil_kind,int image, float katecX,float katecY){
        this.oil_name = oil_name;
        this.price = price;
        this.distance = distance;
        this.oil_kind = oil_kind;
        this.image = image;
        this.katecY = katecY;
        this.katecX = katecX;

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

    public void setKatecX(float latitude){ this.katecX = katecX; }

    public float getKatecX(){ return katecX; }

    public void setKatecY(float KatecY){ this.katecY = KatecY; }

    public float getKatecY(){ return katecY; }




}
