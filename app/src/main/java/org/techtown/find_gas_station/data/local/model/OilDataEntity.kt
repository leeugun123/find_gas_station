package org.techtown.find_gas_station.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class OilConditionEntity(
    @ColumnInfo(name = "oilName")
    var oilName: String,// 기름 종류

    @ColumnInfo(name = "oilRad")
    var oilRad: String,// 반경 범위

    @ColumnInfo(name = "oilSort")
    var oilSort: String, // 정렬기준
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
