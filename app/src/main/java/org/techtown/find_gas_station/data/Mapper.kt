package org.techtown.find_gas_station.data

import org.techtown.find_gas_station.data.local.model.OilConditionEntity
import org.techtown.find_gas_station.data.remote.model.oilavg.OilAveragePriceInfoDto
import org.techtown.find_gas_station.domain.model.OilAveragePriceInfo
import org.techtown.find_gas_station.domain.model.OilCondition

fun OilCondition.toEntity() : OilConditionEntity {
    return OilConditionEntity(
        oilName = this.oilKind,
        oilRad = this.radius,
        oilSort = this.sort
    )
}

fun OilConditionEntity.toDomain() : OilCondition {
    return OilCondition(
        oilKind = this.oilName ?: "",
        radius = this.oilRad ?: "",
        sort = this.oilSort ?: ""
    )
}

fun List<OilAveragePriceInfoDto>.toDomainList() : List<OilAveragePriceInfo> {
    return map { dto ->
        OilAveragePriceInfo(
            date = dto.date,
            oilKind = dto.oilKind,
            oilPrice = dto.oilPrice
        )
    }
}