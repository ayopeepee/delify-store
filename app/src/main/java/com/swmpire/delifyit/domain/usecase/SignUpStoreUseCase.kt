package com.swmpire.delifyit.domain.usecase

import com.swmpire.delifyit.domain.model.StoreModel
import com.swmpire.delifyit.domain.repository.AuthRepository
import com.swmpire.delifyit.domain.repository.FirestoreRepository
import javax.inject.Inject

class SignUpStoreUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) =
        authRepository.firebaseSignUp(email = email, password = password)
}