package org.techtown.find_gas_station;

public class oil_list {

    private String Uid;
    private String oil_name;
    private String price;
    private String distance;
    private String oil_kind;

    private String carWash;//세차장 유무
    private String conStore;//편의점 유무

    private String lotNumberAdd;//지번 주소
    private String roadAdd; //도로명 주소
    private String tel; // 전화번호
    private String sector; // 업종 구분

    private int image;

    private float wgsX;//wgs84 좌표 x
    private float wgsY;//wgs84 좌표 y

    private String actDistance; // 실제 거리
    private String spendTime; //소요시간

    public oil_list(String Uid,String oil_name, String price, String distance,String oil_kind,int image, float wgsX,float wgsY
    ,String carWash,String conStore,String lotNumberAdd,String roadAdd,String tel,String sector,String actDistance,String spendTime){

        this.Uid = Uid;
        this.oil_name = oil_name;
        this.price = price;
        this.distance = distance;
        this.oil_kind = oil_kind;
        this.image = image;
        this.wgsX = wgsX;
        this.wgsY = wgsY;

        this.carWash = carWash;
        this.conStore = conStore;

        this.lotNumberAdd = lotNumberAdd;
        this.roadAdd = roadAdd;
        this.tel = tel;
        this.sector = sector;

        this.actDistance = actDistance;
        this.spendTime = spendTime;

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

    public float getWgs84X(){ return wgsX; }

    public float getWgs84Y(){ return wgsY; }

    public String getCarWash(){return carWash;}

    public String getConStore(){return conStore;}

    public String getLotNumberAdd(){return lotNumberAdd;}

    public String getRoadAdd(){return roadAdd;}

    public String getTel(){return tel;}

    public String getSector(){return sector;}

    public String getActDistance(){return actDistance;}

    public String getSpendTime(){return spendTime;}



}
