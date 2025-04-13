package org.techtown.find_gas_station.domain.usecase

import org.techtown.find_gas_station.domain.model.OilCondition
import org.techtown.find_gas_station.domain.repositoy.LocalRepository
import javax.inject.Inject

class GetLocalOilConditionUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    suspend operator fun invoke(): OilCondition? {
        return localRepository.getOilLocalData()
    }
}