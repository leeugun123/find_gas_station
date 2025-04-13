package org.techtown.find_gas_station.data

import org.techtown.find_gas_station.data.local.model.OilConditionEntity
import org.techtown.find_gas_station.data.remote.model.oilavg.OilAveragePriceInfoDto
import org.techtown.find_gas_station.domain.model.OilAveragePriceInfo
import org.techtown.find_gas_station.domain.model.OilCondition
import org.techtown.find_gas_station.domain.model.StationDetailInfo
import org.techtown.find_gas_station.presentation.common.TotalOilInfoParcelDTO

fun OilCondition.toEntity(): OilConditionEntity {
    return OilConditionEntity(
        oilName = this.oilKind,
        oilRad = this.radius,
        oilSort = this.sort
    )
}

fun OilConditionEntity.toDomain(): OilCondition {
    return OilCondition(
        oilKind = this.oilName,
        radius = this.oilRad,
        sort = this.oilSort
    )
}

fun List<OilAveragePriceInfoDto>.toDomainList(): List<OilAveragePriceInfo> {
    return map { dto ->
        OilAveragePriceInfo(
            date = dto.date,
            oilKind = dto.oilKind,
            oilPrice = dto.oilPrice
        )
    }
}

fun StationDetailInfo.toParableDTO(): TotalOilInfoParcelDTO {
    return TotalOilInfoParcelDTO(
        uid = uid,
        name = name,
        price = price,
        directDistance = directDistance,
        oilKind = oilKind,
        image = image,
        wgs84X = wgs84X,
        wgs84Y = wgs84Y,
        carWash = carWash,
        conStore = conStore,
        lotNumberAdd = lotNumberAdd,
        roadAdd = roadAdd,
        tel = tel,
        sector = sector,
        actualDistance = actualDistance,
        spendTime = spendTime
    )
}



