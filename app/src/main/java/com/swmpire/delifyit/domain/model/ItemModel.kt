package com.swmpire.delifyit.domain.model

import android.net.Uri
import com.google.firebase.firestore.DocumentReference

data class ItemModel(
    val name: String? = null,
    val description: String? = null,
    val category: String? = null,
    val price: Int? = null,
    val imageUrl: String? = null,
    var storeReference: DocumentReference? = null
)
