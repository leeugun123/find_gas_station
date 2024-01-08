package org.techtown.find_gas_station.Data.set

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "set_table")
data class Set(

    @PrimaryKey(autoGenerate = true)
    private var id : Int = 0,

    @ColumnInfo(name = "oil_name")
    private var oilName : String = "B027",// 기름 종류

    @ColumnInfo(name = "oil_rad")
    private var oilRad : String = "1000",// 반경 범위

    @ColumnInfo(name = "oil_sort")
    private var oilSort : String = "1", // 정렬기준

) {
    fun getOilName() = oilName

    fun getOilRad() = oilRad

    fun getOilSort() = oilSort

}