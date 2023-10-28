package com.swmpire.delifyit.domain.usecase

import com.swmpire.delifyit.domain.model.ItemModel
import com.swmpire.delifyit.domain.repository.FirestoreRepository
import javax.inject.Inject

class AddItemUseCase @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) {
    suspend operator fun invoke(
        name: String,
        description: String,
        category: String,
        price: Int
    ) = firestoreRepository.addItem(
            ItemModel(
                name = name,
                description = description,
                category = category,
                price = price
            )
        )

}