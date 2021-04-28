package com.lelloman.simplerss.sources

import com.lelloman.simplerss.domain_local_sources.LocalSourcesRepository
import com.lelloman.simplerss.domain_remote_sources.RemoteSourcesRepository
import com.lelloman.simplerss.ui_source.list.SourcesInteractor
import io.reactivex.rxjava3.core.Observable

private typealias Local = SourcesInteractor.SourceType.Local
private typealias Remote = SourcesInteractor.SourceType.Remote

class UserSourcesAdapter(
    private val localSourcesRepository: LocalSourcesRepository,
    private val remoteSourcesRepository: RemoteSourcesRepository
) {

    private val LocalSourcesRepository.interactorSources
        get() = observeLocalSources()
            .map { localSources ->
                localSources.map { localSource ->
                    SourcesInteractor.Source(
                        id = "${Local.id}_${localSource.id}",
                        name = localSource.name,
                        sourceType = Local
                    )
                }
            }

    private val RemoteSourcesRepository.interactorSources
        get() = observeRemoteSources()
            .map { remoteSources ->
                remoteSources.map { remoteSource ->
                    SourcesInteractor.Source(
                        id = "${Remote.id}_${remoteSource.id}",
                        name = remoteSource.name,
                        sourceType = Remote
                    )
                }
            }

    fun observeSources(): Observable<List<SourcesInteractor.Source>> = Observable.combineLatest(
        localSourcesRepository.interactorSources,
        remoteSourcesRepository.interactorSources,
        { t1, t2 ->
            ArrayList<SourcesInteractor.Source>(t1.size + t2.size).apply {
                addAll(t1)
                addAll(t2)
            }
        }
    )
}