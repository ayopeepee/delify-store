package com.swmpire.delifyit.domain.model

data class StoreModel(
    var storeId: String? = null,
    val email: String,
    val password: String,
    val name: String? = null,
    val description: String? = null,
    val type: String? = null,
    val address: String? = null,
    val location: LocationModel? = null
)
