package com.swmpire.delifyit.domain.usecase

import com.swmpire.delifyit.domain.model.OrderModel
import com.swmpire.delifyit.domain.repository.FirestoreRepository
import javax.inject.Inject

class CreateOrderUseCase @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) {
    suspend operator fun invoke() = firestoreRepository.createOrder(
        OrderModel(
            orderStatus = "Создан"
        )
    )
}