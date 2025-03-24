package org.techtown.find_gas_station.data

import org.techtown.find_gas_station.data.local.model.OilConditionEntity
import org.techtown.find_gas_station.presentation.common.TotalOilInfoParcelDTO
import org.techtown.find_gas_station.data.remote.model.oilavg.OilAveragePriceInfoDto
import org.techtown.find_gas_station.domain.model.OilAveragePriceInfo
import org.techtown.find_gas_station.domain.model.OilCondition
import org.techtown.find_gas_station.domain.model.TotalOilInfo

fun OilCondition.toEntity() : OilConditionEntity {
    return OilConditionEntity(
        oilName = this.oilKind,
        oilRad = this.radius,
        oilSort = this.sort
    )
}

fun OilConditionEntity?.toDomain(): OilCondition {
    return this?.let {
        OilCondition(
            oilKind = it.oilName ?: "B027",
            radius = it.oilRad ?: "1000",
            sort = it.oilSort ?: "1"
        )
    } ?: OilCondition(
        oilKind = "B027",
        radius = "1000",
        sort = "1"
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

fun TotalOilInfo.toParableDTO(): TotalOilInfoParcelDTO {
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



