package com.swmpire.delifyit.data.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.swmpire.delifyit.domain.model.NetworkResult
import com.swmpire.delifyit.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    private val TAG = "AuthRepositoryImpl"

    override val currentStore: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun firebaseSignUp(
        email: String,
        password: String
    ): Flow<NetworkResult<Boolean>> {
        return flow {
            var isSuccess = false
            emit(NetworkResult.Loading())

            try {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        isSuccess = if (task.isSuccessful) {
                            Log.d(TAG, "firebaseSignUp: success")
                            currentStore != null
                        } else {
                            Log.e(TAG, "firebaseSignUp: fail", task.exception)
                            false
                        }
                    }.await()
                if (isSuccess) {
                    emit(NetworkResult.Success(true))
                } else {
                    emit(NetworkResult.Error("something went wrong"))
                }

            } catch (e: Exception) {
                emit(NetworkResult.Error(message = e.localizedMessage ?: "something went wrong"))

            }
        }
    }

    override suspend fun firebaseSignIn(
        email: String,
        password: String
    ): Flow<NetworkResult<Boolean>> {
        return flow {
            var isSuccess = false

            emit(NetworkResult.Loading())

            try {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        isSuccess = if (task.isSuccessful) {
                            Log.d(TAG, "signInWithEmailAndPassword: success")
                            firebaseAuth.currentUser != null
                        } else {
                            Log.e(TAG, "signInWithEmailAndPassword: fail", task.exception)
                            false
                        }
                    }.await()

                if (isSuccess) {
                    emit(NetworkResult.Success(true))
                } else {
                    emit(NetworkResult.Error("something went wrong"))
                }
            } catch (e: Exception) {
                emit(NetworkResult.Error(message = e.localizedMessage ?: "something went wrong"))
            }
        }
    }

    override suspend fun firebaseResetPassword(email: String): Flow<NetworkResult<Boolean>> {
        return flow {
            var isSuccess = false

            emit(NetworkResult.Loading())

            try {
                firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        isSuccess = if (task.isSuccessful) {
                            Log.d(TAG, "firebaseResetPassword: success")
                            true
                        } else {
                            Log.e(TAG, "firebaseResetPassword: fail", task.exception)
                            false
                        }
                    }.await()

                if (isSuccess) {
                    emit(NetworkResult.Success(true))
                } else {
                    emit(NetworkResult.Error("something went wrong"))
                }
            } catch (e: Exception) {
                emit(NetworkResult.Error(message = e.localizedMessage ?: "something went wrong"))
            }

        }
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }
}