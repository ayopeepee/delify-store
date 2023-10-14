package com.swmpire.delifyit.domain.usecase

import com.swmpire.delifyit.domain.repository.AuthRepository
import javax.inject.Inject

class SignInStoreUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) =
        authRepository.firebaseSignIn(email = email, password = password)
}