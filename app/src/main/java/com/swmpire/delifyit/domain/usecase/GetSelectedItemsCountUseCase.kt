package com.swmpire.delifyit.domain.usecase

import com.swmpire.delifyit.domain.repository.DatabaseRepository
import javax.inject.Inject

class GetSelectedItemsCountUseCase @Inject constructor(
    private val databaseRepository: DatabaseRepository
) {
    suspend operator fun invoke() : Int = databaseRepository.getSelectedItemsCount()
}