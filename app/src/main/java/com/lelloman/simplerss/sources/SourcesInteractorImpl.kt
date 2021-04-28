package com.lelloman.simplerss.sources

import com.lelloman.simplerss.R
import com.lelloman.simplerss.navigation.NavigationEventProcessor
import com.lelloman.simplerss.ui_source.list.SourcesInteractor
import io.reactivex.rxjava3.core.Observable

class SourcesInteractorImpl(
    private val userSourcesAdapter: UserSourcesAdapter,
    private val navigationEventProcessor: NavigationEventProcessor
) : SourcesInteractor {

    override fun observeSources(): Observable<List<SourcesInteractor.Source>> =
//        userSourcesAdapter.observeSources()
        Observable.just(Array(100) {
            SourcesInteractor.Source(
                id = it.toString(),
                name = "Source $it",
                sourceType = if (it % 2 == 0) SourcesInteractor.SourceType.Remote else SourcesInteractor.SourceType.Local
            )
        }.toList())

    override fun navigateToAddLocalSource() = navigationEventProcessor {
        it.navigate(R.id.action_sourcesFragment_to_localSourceFragment)
    }

    override fun navigateToAddRemoteSource() = navigationEventProcessor {
        it.navigate(R.id.action_sourcesFragment_to_remoteSourceFragment)
    }

    override fun navigateToDiscover() {

    }
}