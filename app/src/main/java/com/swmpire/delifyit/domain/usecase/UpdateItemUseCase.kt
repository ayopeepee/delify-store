package com.swmpire.delifyit.domain.usecase

import com.swmpire.delifyit.domain.model.ItemModel
import com.swmpire.delifyit.domain.repository.FirestoreRepository
import javax.inject.Inject

class UpdateItemUseCase @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) {
    suspend operator fun invoke(item: ItemModel) = firestoreRepository.updateItem(item = item)
}