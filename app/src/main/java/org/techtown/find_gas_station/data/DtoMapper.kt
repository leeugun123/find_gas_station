package org.techtown.find_gas_station.data

import org.techtown.find_gas_station.data.remote.model.oilavg.OilAveragePriceInfoDto
import org.techtown.find_gas_station.domain.model.OilAveragePriceInfo

fun List<OilAveragePriceInfoDto>.toDomainList() : List<OilAveragePriceInfo> {
    return map { dto ->
        OilAveragePriceInfo(
            date = dto.date,
            oilKind = dto.oilKind,
            oilPrice = dto.oilPrice
        )
    }
}