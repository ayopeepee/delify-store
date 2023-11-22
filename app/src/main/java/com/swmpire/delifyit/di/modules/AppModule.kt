package com.swmpire.delifyit.di.modules

import android.content.Context
import com.swmpire.delifyit.presentation.ui.main.tabs.items.utils.InputTextChangeObserver
import com.swmpire.delifyit.presentation.ui.main.tabs.items.utils.TextValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideTextValidator(@ApplicationContext context: Context) : TextValidator =
        TextValidator(context)

    @Provides
    @Singleton
    fun provideInputTextChangeObserver(validator: TextValidator) : InputTextChangeObserver =
        InputTextChangeObserver(validator)
}