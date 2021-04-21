package com.lelloman.simplerss.viewmodel

import android.content.Context
import com.lelloman.simplerss.ui_base.ResourcesProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    fun provideResourcesProvider(@ApplicationContext context: Context): ResourcesProvider {
        return ContextResourcesProvider(context)
    }

}