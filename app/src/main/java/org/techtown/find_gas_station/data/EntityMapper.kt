package org.techtown.find_gas_station.data

import org.techtown.find_gas_station.data.local.model.OilDataEntity
import org.techtown.find_gas_station.domain.model.OilCondition

fun OilCondition.toEntity() : OilDataEntity{
    return OilDataEntity(
        oilName = this.oilKind,
        oilRad = this.radius,
        oilSort = this.sort
    )
}

fun OilDataEntity.toDomain() : OilCondition{
    return OilCondition(
        oilKind = this.oilName ?: "",
        radius = this.oilRad ?: "",
        sort = this.oilSort ?: ""
    )
}