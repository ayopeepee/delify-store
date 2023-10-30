package com.swmpire.delifyit.domain.usecase

import android.net.Uri
import com.swmpire.delifyit.domain.repository.StorageRepository
import javax.inject.Inject

class AddItemImageUseCase @Inject constructor(
    private val storageRepository: StorageRepository
) {
    suspend operator fun invoke(uri: Uri) = storageRepository.addItemImage(uri)
}