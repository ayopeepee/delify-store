package com.swmpire.delifyit.domain.usecase

import com.swmpire.delifyit.domain.repository.DatabaseRepository
import javax.inject.Inject

class DeselectAllItemsUseCase @Inject constructor(
    private val databaseRepository: DatabaseRepository
) {
    suspend operator fun invoke() = databaseRepository.deselectAllItems()
}