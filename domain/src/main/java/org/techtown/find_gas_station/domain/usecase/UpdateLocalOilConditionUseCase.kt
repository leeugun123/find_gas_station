package org.techtown.find_gas_station.domain.usecase

import org.techtown.find_gas_station.domain.model.OilCondition
import org.techtown.find_gas_station.domain.repositoy.LocalRepository
import javax.inject.Inject

class UpdateLocalOilConditionUseCase @Inject constructor(
    private val setRepository: LocalRepository
) {
    suspend operator fun invoke(oilCondition: OilCondition) {
        setRepository.deleteAll()
        setRepository.insert(oilCondition)
    }
}