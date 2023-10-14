package com.swmpire.delifyit.domain.usecase

import com.swmpire.delifyit.domain.repository.AuthRepository
import javax.inject.Inject

class SignOutStoreUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke() {
        authRepository.signOut()
    }
}