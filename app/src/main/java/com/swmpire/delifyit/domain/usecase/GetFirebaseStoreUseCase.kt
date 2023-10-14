package com.swmpire.delifyit.domain.usecase

import com.google.firebase.auth.FirebaseUser
import com.swmpire.delifyit.domain.repository.AuthRepository
import javax.inject.Inject

class GetFirebaseStoreUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): FirebaseUser? {
        return authRepository.currentStore
    }
}