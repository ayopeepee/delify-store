package com.swmpire.delifyit.domain.usecase

import com.swmpire.delifyit.domain.repository.FirestoreRepository
import javax.inject.Inject

class PlaceOrderUseCase @Inject constructor(
    private val firestoreRepository: FirestoreRepository
){
    suspend operator fun invoke(id: String) = firestoreRepository.placeOrder(id)
}