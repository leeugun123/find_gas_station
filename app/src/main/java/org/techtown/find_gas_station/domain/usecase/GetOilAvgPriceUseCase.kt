package org.techtown.find_gas_station.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.techtown.find_gas_station.di.RepositoryModule
import org.techtown.find_gas_station.domain.model.OilAveragePriceInfo

class GetOilAvgPriceUseCase {

    private val oilAvgRepository = RepositoryModule.provideGetOilAvgRepository()

    suspend operator fun invoke(prodCd : String) : Flow<List<OilAveragePriceInfo>> {
        return oilAvgRepository.getOilAvg(prodCd)
    }
}