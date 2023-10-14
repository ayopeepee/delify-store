package com.swmpire.delifyit.domain.usecase

import com.swmpire.delifyit.domain.model.StoreModel
import com.swmpire.delifyit.domain.repository.AuthRepository
import javax.inject.Inject

class SignUpStoreUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(storeModel: StoreModel) =
        authRepository.firebaseSignUp(store = storeModel)
}