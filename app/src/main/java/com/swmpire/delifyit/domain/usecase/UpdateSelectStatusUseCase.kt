package com.swmpire.delifyit.domain.usecase

import com.swmpire.delifyit.domain.repository.DatabaseRepository
import javax.inject.Inject
class UpdateSelectStatusUseCase @Inject constructor(
    private val databaseRepository: DatabaseRepository
) {
    suspend operator fun invoke(id: String?, isSelected: Boolean) {
        if (!id.isNullOrBlank()) databaseRepository.updateSelectStatus(id, isSelected)
    }
}

