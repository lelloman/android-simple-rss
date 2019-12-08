package com.lelloman.simplerss.di

import com.lelloman.common.di.KoinModuleFactory
import com.lelloman.common.di.qualifiers.IoScheduler
import com.lelloman.simplerss.ui.common.repository.ArticlesRepository
import com.lelloman.simplerss.ui.common.repository.DiscoverRepository
import com.lelloman.simplerss.ui.common.repository.SourcesRepository
import org.koin.dsl.module

class RepositoryModuleFactory : KoinModuleFactory {
    override fun makeKoinModule(override: Boolean) = module(override=override) {
        single {
            ArticlesRepository(
                articlesDao = get(),
                feedRefresher = get()
            )
        }
        single {
            DiscoverRepository(
                ioScheduler = get(IoScheduler),
                sourcesDao = get(),
                feedFinder = get(),
                loggerFactory = get()
            )
        }
        single {
            SourcesRepository(
                ioScheduler = get(IoScheduler),
                sourcesDao = get(),
                feedRefresher = get(),
                articlesDao = get()
            )
        }
    }
}