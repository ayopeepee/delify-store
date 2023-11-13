package com.swmpire.delifyit.domain.usecase

import com.swmpire.delifyit.domain.model.StoreModel
import com.swmpire.delifyit.domain.repository.AuthRepository
import com.swmpire.delifyit.domain.repository.FirestoreRepository
import javax.inject.Inject

class CreateFirestoreStoreUseCase @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) {
    suspend operator fun invoke(name: String, description: String, address: String, type: String, profilePictureUrl: String) =
        firestoreRepository.createStore(
            StoreModel(
                name = name,
                description = description,
                type = type,
                address = address,
                profilePictureUrl = profilePictureUrl
            )
        )
}