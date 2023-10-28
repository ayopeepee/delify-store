package com.swmpire.delifyit.di.modules

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.swmpire.delifyit.data.firebase.AuthRepositoryImpl
import com.swmpire.delifyit.data.firebase.FirestoreRepositoryImpl
import com.swmpire.delifyit.domain.repository.AuthRepository
import com.swmpire.delifyit.domain.repository.FirestoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()


    @Provides
    @Singleton
    fun provideFirebaseFirestore() = FirebaseFirestore.getInstance()


    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth
    ) : AuthRepository = AuthRepositoryImpl(firebaseAuth = firebaseAuth)

    @Provides
    @Singleton
    fun provideFirestoreRepository(
        firebaseAuth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore
    ) : FirestoreRepository = FirestoreRepositoryImpl(
        firebaseAuth = firebaseAuth,
        firebaseFirestore = firebaseFirestore
    )

}