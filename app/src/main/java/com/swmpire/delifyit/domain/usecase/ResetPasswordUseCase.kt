package com.swmpire.delifyit.domain.usecase

import com.swmpire.delifyit.domain.repository.AuthRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String) = authRepository.firebaseResetPassword(email)
}