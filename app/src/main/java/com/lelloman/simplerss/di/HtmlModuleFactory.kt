package com.lelloman.simplerss.di

import com.lelloman.common.di.KoinModuleFactory
import com.lelloman.simplerss.html.HtmlParser
import com.lelloman.simplerss.html.HtmlSpanner
import org.koin.dsl.module

class HtmlModuleFactory : KoinModuleFactory {

    override fun makeKoinModule(override: Boolean) = module(override=override) {
        single { HtmlParser() }
        factory { HtmlSpanner() }
    }
}