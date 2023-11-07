package com.swmpire.delifyit.di.modules

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.swmpire.delifyit.data.room.DatabaseRepositoryImpl
import com.swmpire.delifyit.data.room.ItemDao
import com.swmpire.delifyit.data.room.ItemDatabase
import com.swmpire.delifyit.domain.repository.DatabaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ItemDatabase =
        Room.databaseBuilder(context, ItemDatabase::class.java, "item_database").build()

    @Provides
    fun provideItemDao(itemDatabase: ItemDatabase) = itemDatabase.itemDao()

    @Provides
    @Singleton
    fun provideDatabaseRepository(itemDao: ItemDao): DatabaseRepository =
        DatabaseRepositoryImpl(itemDao = itemDao)
}