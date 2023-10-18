package com.swmpire.delifyit.data.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.swmpire.delifyit.domain.model.NetworkResult
import com.swmpire.delifyit.domain.model.StoreModel
import com.swmpire.delifyit.domain.repository.AuthRepository
import com.swmpire.delifyit.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : AuthRepository {

    private val TAG = "AuthRepositoryImpl"

    override val currentStore: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun firebaseSignUp(store: StoreModel): Flow<NetworkResult<Boolean>> {
        return flow {

            emit(NetworkResult.Loading())

            try {
                val result =
                    firebaseAuth.createUserWithEmailAndPassword(store.email, store.password).await()

                if (result.user != null) {
                    val firebaseStore = firebaseAuth.currentUser
                    if (firebaseStore != null) {
                        store.storeId = firebaseStore.uid
                        firebaseFirestore
                            .collection(Constants.STORES)
                            .document(firebaseStore.uid)
                            .set(store)
                            .await()
                        emit(NetworkResult.Success(true))
                    } else {
                        emit(NetworkResult.Error("something went wrong on creating new user (firestore)"))
                    }
                } else {
                    emit(NetworkResult.Error("something went wrong on creating new user (auth)"))
                }
            } catch (e: Exception) {
                emit(
                    NetworkResult.Error(message = e.localizedMessage ?: "something went wrong")
                )
            }
        }
    }

    override suspend fun firebaseSignIn(
        email: String,
        password: String
    ): Flow<NetworkResult<Boolean>> {
        // TODO: change the firebase call like in firebaseSignUp so the race condition doesn't appear!!!
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

    override fun signOut() {
        firebaseAuth.signOut()
    }
}