package org.techtown.find_gas_station.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.techtown.find_gas_station.domain.model.OilAveragePriceInfo
import org.techtown.find_gas_station.domain.repositoy.OilAvgRepository
import javax.inject.Inject

class GetOilAvgPriceUseCase @Inject constructor(
    private val oilAvgRepository: OilAvgRepository
) {
    suspend operator fun invoke(prodCd: String): Flow<List<OilAveragePriceInfo>> =
        oilAvgRepository.getOilAvg(prodCd)
}