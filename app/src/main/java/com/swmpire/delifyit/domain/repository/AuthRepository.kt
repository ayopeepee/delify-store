package com.swmpire.delifyit.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.swmpire.delifyit.domain.model.NetworkResult
import com.swmpire.delifyit.domain.model.StoreModel
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    val currentStore : FirebaseUser?

    suspend fun firebaseSignUp(store: StoreModel): Flow<NetworkResult<Boolean>>

    suspend fun firebaseSignIn(email: String, password: String): Flow<NetworkResult<Boolean>>

    suspend fun firebaseResetPassword(email: String): Flow<NetworkResult<Boolean>>

    fun signOut()
}