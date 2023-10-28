package com.swmpire.delifyit.domain.model

import com.google.firebase.firestore.DocumentReference

data class ItemModel(
    val name: String,
    val description: String,
    val category: String,
    val price: Int,
    var storeReference: DocumentReference? = null
)
