package com.lelloman.simplerss.html

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class HtmlModule {

    @Provides
    @Singleton
    fun provideHtmlParser() = com.lelloman.simplerss.html.HtmlParser()
}