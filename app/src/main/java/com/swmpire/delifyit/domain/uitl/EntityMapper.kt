package com.swmpire.delifyit.domain.uitl

import com.swmpire.delifyit.data.room.ItemEntity

interface EntityMapper <ItemModel, ItemEntity> {
    fun mapFromModelToEntity(itemModel: ItemModel) : ItemEntity
    fun mapFromEntityToModel(itemEntity: ItemEntity) : ItemModel
}