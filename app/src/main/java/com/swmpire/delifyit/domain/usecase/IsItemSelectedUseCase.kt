package com.swmpire.delifyit.domain.usecase

import com.swmpire.delifyit.domain.repository.DatabaseRepository
import javax.inject.Inject

class IsItemSelectedUseCase @Inject constructor(
    private val databaseRepository: DatabaseRepository
) {
    suspend operator fun invoke(id: String?): Boolean {
        return if (id != null) databaseRepository.isItemSelected(id)
        else false
    }
}