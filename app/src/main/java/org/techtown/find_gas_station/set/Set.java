package org.techtown.find_gas_station.set;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

//setData
@Entity(tableName = "set_table")
public class Set {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "oil_name")
    private String oil_name = "B027"; // 기름 종류

    @ColumnInfo(name = "oil_rad")
    private String oil_rad = "1000"; // 반경 범위

    @ColumnInfo(name = "oil_sort")
    private String oil_sort = "1"; // 정렬기준

    public Set(){

    }

    public Set(String oil_name,String oil_rad, String oil_sort){
        this.oil_name = oil_name;
        this.oil_rad = oil_rad;
        this.oil_sort = oil_sort;
    }


    public String getOil_name(){
        return oil_name;
    }

    public void setOil_name(String oil_name) {
        this.oil_name = oil_name;
    }

    public String getOil_rad(){
        return oil_rad;
    }

    public void setOil_rad(String oil_rad){
        this.oil_rad = oil_rad;
    }

    public String getOil_sort(){
        return oil_sort;
    }

    public void setOil_sort(String oil_sort){
        this.oil_sort = oil_sort;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
