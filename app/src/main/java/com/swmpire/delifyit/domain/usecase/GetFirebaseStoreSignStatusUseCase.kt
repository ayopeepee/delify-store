package com.swmpire.delifyit.domain.usecase

import com.google.firebase.auth.FirebaseUser
import com.swmpire.delifyit.domain.repository.AuthRepository
import javax.inject.Inject

class GetFirebaseStoreSignStatusUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke() : Boolean{
        if (authRepository.currentStore != null)
            return true
        return false
    }
}