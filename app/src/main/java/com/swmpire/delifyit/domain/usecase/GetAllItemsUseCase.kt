package com.swmpire.delifyit.domain.usecase

import com.swmpire.delifyit.domain.repository.FirestoreRepository
import javax.inject.Inject

class GetAllItemsUseCase @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) {
    suspend operator fun invoke() = firestoreRepository.getAllItems()
}