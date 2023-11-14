package com.swmpire.delifyit.domain.model

data class StoreModel(
    @field:JvmField // cause of prefix "is"
    var isVerified: Boolean? = null,
    val name: String? = null,
    val description: String? = null,
    val type: String? = null,
    val address: String? = null,
    val profilePictureUrl: String? = null
)
