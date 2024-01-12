package org.techtown.find_gas_station.Data.set

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class OilData(

    @ColumnInfo(name = "oilName")
    var oilName : String = "B027",// 기름 종류

    @ColumnInfo(name = "oilRad")
    var oilRad : String = "1000",// 반경 범위

    @ColumnInfo(name = "oilSort")
    var oilSort : String = "1", // 정렬기준

) {

    @PrimaryKey(autoGenerate = true)
    var id : Int = 0

}