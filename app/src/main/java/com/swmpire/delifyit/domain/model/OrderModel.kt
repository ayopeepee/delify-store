package com.swmpire.delifyit.domain.model

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class OrderModel(
    var id: String? = null,
    var orderStatus: String? = null,
    @ServerTimestamp var createOrderDate: Date? = null,
    var readyOrderDate: Date? = null,
    var storeReference: DocumentReference? = null,
    var price: Int? = null,
    var items: Map<String, Int>? = null
)
