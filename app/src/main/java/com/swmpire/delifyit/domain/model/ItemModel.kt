package com.swmpire.delifyit.domain.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentReference
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class ItemModel(
    var id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val category: String? = null,
    val price: Int? = null,
    val imageUrl: String? = null,
    var storeReference: @RawValue DocumentReference? = null
) : Parcelable
