package com.swmpire.delifyit.domain.usecase

import com.swmpire.delifyit.domain.model.StoreModel
import com.swmpire.delifyit.domain.repository.AuthRepository
import com.swmpire.delifyit.domain.repository.FirestoreRepository
import javax.inject.Inject

class CreateFirestoreStoreUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val firestoreRepository: FirestoreRepository
) {
    suspend operator fun invoke(name: String, description: String, address: String, type: String) =
        firestoreRepository.createStore(
            StoreModel(
                storeId = authRepository.currentStore?.uid,
                name = name,
                description = description,
                type = type,
                address = address
            )
        )
}