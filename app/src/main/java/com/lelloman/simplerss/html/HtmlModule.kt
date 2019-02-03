package com.lelloman.simplerss.html

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class HtmlModule {

    @Provides
    @Singleton
    fun provideHtmlParser() = HtmlParser()

    @Provides
    fun provideHtmlSpanner() = HtmlSpanner()
}