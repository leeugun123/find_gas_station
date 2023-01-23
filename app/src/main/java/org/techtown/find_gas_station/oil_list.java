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

    public oil_list(String Uid,String oil_name, String price, String distance,String oil_kind,int image, float wgsX,float wgsY
    ,String carWash,String conStore,String lotNumberAdd,String roadAdd,String tel,String sector){

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

    public float getWgs84X(){ return wgsX; }

    public void setKatecY(float wgsY){ this.wgsY = wgsY; }

    public float getWgs84Y(){ return wgsY; }

    public void setUid(String Uid){
        this.Uid = Uid;
    }

    public String getUid(){
        return Uid;
    }

    public void setCarWash(String carWash){this.carWash = carWash;}

    public String getCarWash(){return carWash;}

    public void setConStore(String store){this.conStore = store;}

    public String getConStore(){return conStore;}

    public void setLotNumberAdd(String lotNumberAdd){
        this.lotNumberAdd = lotNumberAdd;
    }

    public String getLotNumberAdd(){return lotNumberAdd;}

    public void setRoadAdd(String roadAdd){this.roadAdd = roadAdd;}

    public String getRoadAdd(){return roadAdd;}

    public void setTel(String tel){this.tel = tel;}

    public String getTel(){return tel;}

    public void setSector(String sector){this.sector = sector;}

    public String getSector(){return sector;}


}
