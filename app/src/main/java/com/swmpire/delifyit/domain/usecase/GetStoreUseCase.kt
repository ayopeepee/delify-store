package com.swmpire.delifyit.domain.usecase

import com.swmpire.delifyit.domain.repository.FirestoreRepository
import javax.inject.Inject

class GetStoreUseCase @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) {
    suspend operator fun invoke() = firestoreRepository.getStore()
}