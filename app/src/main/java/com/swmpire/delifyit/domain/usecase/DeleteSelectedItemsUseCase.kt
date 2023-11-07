package com.swmpire.delifyit.domain.usecase

import com.swmpire.delifyit.domain.repository.DatabaseRepository
import com.swmpire.delifyit.domain.repository.FirestoreRepository
import javax.inject.Inject

class DeleteSelectedItemsUseCase @Inject constructor(
    private val firestoreRepository: FirestoreRepository
){
    suspend operator fun invoke() = firestoreRepository.deleteSelectedItems()
}