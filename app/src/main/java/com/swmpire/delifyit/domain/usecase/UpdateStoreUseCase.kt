package com.swmpire.delifyit.domain.usecase

import com.swmpire.delifyit.domain.model.StoreModel
import com.swmpire.delifyit.domain.repository.FirestoreRepository
import javax.inject.Inject

class UpdateStoreUseCase @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) {
    suspend operator fun invoke(storeModel: StoreModel) = firestoreRepository.updateStore(storeModel)
}