package com.swmpire.delifyit.di.modules

import com.swmpire.delifyit.domain.repository.AuthRepository
import com.swmpire.delifyit.domain.usecase.GetFirebaseStoreSignStatusUseCase
import com.swmpire.delifyit.domain.usecase.GetFirebaseStoreUseCase
import com.swmpire.delifyit.domain.usecase.SignInStoreUseCase
import com.swmpire.delifyit.domain.usecase.SignOutStoreUseCase
import com.swmpire.delifyit.domain.usecase.SignUpStoreUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {

    @Provides
    fun provideSignUpNewStoreUseCase(authRepository: AuthRepository) =
        SignUpStoreUseCase(authRepository)

    @Provides
    fun provideSignInStoreUseCase(authRepository: AuthRepository) =
        SignInStoreUseCase(authRepository)

    @Provides
    fun provideGetFirebaseStoreUseCase(authRepository: AuthRepository) =
        GetFirebaseStoreUseCase(authRepository)

    @Provides
    fun provideSignOutStoreUseCase(authRepository: AuthRepository) =
        SignOutStoreUseCase(authRepository)

    @Provides
    fun provideGetFirebaseStoreSignStatusUseCase(authRepository: AuthRepository) =
        GetFirebaseStoreSignStatusUseCase(authRepository)
}