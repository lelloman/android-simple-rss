package com.lelloman.simplerss.navigation

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NavigationModule {

    @Provides
    @Singleton
    fun provideNavigationEventProcessorImpl() = NavigationEventProcessorImpl()

    @Provides
    @Singleton
    fun provideNavigationEventProcessor(impl: NavigationEventProcessorImpl): NavigationEventProcessor = impl

    @Provides
    fun provideNavControllerHolder(navigationEventProcessor: NavigationEventProcessorImpl): NavControllerHolder =
        navigationEventProcessor
}