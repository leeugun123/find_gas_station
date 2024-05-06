package org.techtown.find_gas_station.domain.station.usecase

import org.techtown.find_gas_station.domain.station.model.TotalOilInfo
import org.techtown.find_gas_station.presentation.di.RepositoryModule

class GetStationListUseCase {

    private val stationInfoRepository = RepositoryModule.provideStationInfoRepository()
    // TODO("Hilt로 Injection 주입 해야 함")
    operator fun invoke(): MutableList<TotalOilInfo> = stationInfoRepository.getStationList()
}