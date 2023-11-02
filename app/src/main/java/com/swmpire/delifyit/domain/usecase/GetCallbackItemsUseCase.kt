package com.swmpire.delifyit.domain.usecase

import com.swmpire.delifyit.domain.repository.FirestoreRepository
import javax.inject.Inject

class GetCallbackItemsUseCase @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) {
    operator fun invoke() = firestoreRepository.getItemsCallback()
}