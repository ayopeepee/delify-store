package com.swmpire.delifyit.domain.model

data class StoreModel(
    val storeId: String?,
    @field:JvmField // cause of prefix "is"
    var isVerified: Boolean = false,
    val name: String,
    val description: String,
    val type: String,
    val address: String
)
