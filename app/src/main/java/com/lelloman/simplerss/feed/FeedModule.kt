package com.lelloman.simplerss.feed

import com.lelloman.simplerss.R
import com.lelloman.simplerss.navigation.NavigationEventProcessor
import com.lelloman.simplerss.ui_feed.FeedInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.rxjava3.core.Single

@Module
@InstallIn(ViewModelComponent::class)
object FeedModule {

    @Provides
    @ViewModelScoped
    fun provideFeedInteractor(navigationEventProcessor: NavigationEventProcessor): FeedInteractor {
        return object : FeedInteractor {
            override fun loadFeed(): Single<List<FeedInteractor.FeedItem>> {
                TODO("Not yet implemented")
            }

            override fun goToAbout() = navigationEventProcessor {
                it.navigate(R.id.action_feedFragment_to_aboutFragment)
            }

            override fun goToSettings() = navigationEventProcessor {
                it.navigate(R.id.action_feedFragment_to_settingsFragment)
            }
        }
    }
}