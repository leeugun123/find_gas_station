package org.techtown.find_gas_station.domain.usecase

import org.techtown.find_gas_station.di.RepositoryModule
import org.techtown.find_gas_station.domain.model.OilCondition
import org.techtown.find_gas_station.domain.repositoy.LocalRepository

class GetLocalOilConditionUseCase {

    private val setRepository: LocalRepository = RepositoryModule.provideSetRepository()

    suspend operator fun invoke() : OilCondition? {
        return setRepository.getOilLocalData()
    }
}