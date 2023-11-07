package com.swmpire.delifyit.di.modules

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.swmpire.delifyit.data.firebase.AuthRepositoryImpl
import com.swmpire.delifyit.data.firebase.FirestoreRepositoryImpl
import com.swmpire.delifyit.data.firebase.StorageRepositoryImpl
import com.swmpire.delifyit.data.room.ItemDao
import com.swmpire.delifyit.data.room.ItemDatabase
import com.swmpire.delifyit.domain.repository.AuthRepository
import com.swmpire.delifyit.domain.repository.FirestoreRepository
import com.swmpire.delifyit.domain.repository.StorageRepository
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
    fun provideFirebaseStorage() = FirebaseStorage.getInstance()


    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth
    ): AuthRepository = AuthRepositoryImpl(firebaseAuth = firebaseAuth)

    @Provides
    @Singleton
    fun provideFirestoreRepository(
        firebaseAuth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore,
        itemDao: ItemDao
    ): FirestoreRepository = FirestoreRepositoryImpl(
        firebaseAuth = firebaseAuth,
        firebaseFirestore = firebaseFirestore,
        itemDao = itemDao
    )

    @Provides
    @Singleton
    fun provideStorageRepository(
        firebaseStorage: FirebaseStorage,
        firebaseAuth: FirebaseAuth
    ): StorageRepository =
        StorageRepositoryImpl(
            firebaseStorage = firebaseStorage,
            firebaseAuth = firebaseAuth
        )
}