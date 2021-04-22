package com.lelloman.simplerss.feed

import com.lelloman.simplerss.R
import com.lelloman.simplerss.navigation.NavigationEventProcessor
import com.lelloman.simplerss.ui_feed.model.FeedInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeUnit

@Module
@InstallIn(ViewModelComponent::class)
object FeedModule {

    @Provides
    @ViewModelScoped
    fun provideFeedInteractor(navigationEventProcessor: NavigationEventProcessor): FeedInteractor {
        return object : FeedInteractor {
            override fun loadFeed(): Single<List<FeedInteractor.FeedItem>> {
                return Single.timer(2, TimeUnit.SECONDS)
                    .map {
                        Array(50) {
                            object : FeedInteractor.FeedItem {
                                override val id: String = "id $it"
                                override val title: String = "title $it"
                                override val body: String = "body $it"
                                override val url: String = "url $it"
                            }
                        }.toList()
                    }
            }

            override fun refreshFeed(): Completable {
                return Completable.timer(2, TimeUnit.SECONDS)
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