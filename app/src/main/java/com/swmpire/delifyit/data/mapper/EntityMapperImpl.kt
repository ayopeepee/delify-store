package com.swmpire.delifyit.data.mapper

import com.swmpire.delifyit.data.room.ItemEntity
import com.swmpire.delifyit.domain.model.ItemModel
import com.swmpire.delifyit.domain.uitl.EntityMapper

object EntityMapperImpl : EntityMapper<ItemModel, ItemEntity> {
    override fun mapFromModelToEntity(itemModel: ItemModel): ItemEntity {
        return ItemEntity(
            id = itemModel.id ?: "",
            name = itemModel.name,
            description = itemModel.description,
            category = itemModel.category,
            price = itemModel.price,
            imageUrl = itemModel.imageUrl
        )
    }

    override fun mapFromEntityToModel(itemEntity: ItemEntity): ItemModel {
        return ItemModel(
            id = itemEntity.id,
            name = itemEntity.name,
            description = itemEntity.description,
            category = itemEntity.category,
            price = itemEntity.price
        )
    }
}