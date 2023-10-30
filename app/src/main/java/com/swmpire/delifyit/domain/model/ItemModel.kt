package com.swmpire.delifyit.domain.model

import android.net.Uri
import com.google.firebase.firestore.DocumentReference

data class ItemModel(
    val name: String,
    val description: String,
    val category: String,
    val price: Int,
    val imageUrl: String,
    var storeReference: DocumentReference? = null
)
