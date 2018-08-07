package com.lelloman.read.html

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class HtmlModule {

    @Provides
    @Singleton
    fun provideHtmlParser() = HtmlParser()
}