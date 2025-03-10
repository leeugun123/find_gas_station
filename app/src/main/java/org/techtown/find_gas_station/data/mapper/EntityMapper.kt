package org.techtown.find_gas_station.data.mapper

import org.techtown.find_gas_station.data.local.model.OilConditionEntity
import org.techtown.find_gas_station.domain.model.OilCondition

fun OilCondition.toEntity() : OilConditionEntity {
    return OilConditionEntity(
        oilName = this.oilKind,
        oilRad = this.radius,
        oilSort = this.sort
    )
}

fun OilConditionEntity.toDomain() : OilCondition{
    return OilCondition(
        oilKind = this.oilName ?: "",
        radius = this.oilRad ?: "",
        sort = this.oilSort ?: ""
    )
}