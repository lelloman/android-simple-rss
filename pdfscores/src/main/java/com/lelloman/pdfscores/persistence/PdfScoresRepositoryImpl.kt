package com.lelloman.pdfscores.persistence

import com.lelloman.pdfscores.persistence.assets.AssetsPdfScoresProvider
import com.lelloman.pdfscores.persistence.assets.AssetsPdfScoresProviderFactory
import com.lelloman.pdfscores.persistence.db.AuthorsDao
import com.lelloman.pdfscores.persistence.db.PdfScoresDao
import com.lelloman.pdfscores.persistence.model.Author
import com.lelloman.pdfscores.persistence.model.PdfScore
import com.lelloman.pdfscores.publicapi.PublicPdfScoresAppsFinder
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction

class PdfScoresRepositoryImpl(
    private val pdfScoresDao: PdfScoresDao,
    private val authorsDao: AuthorsDao,
    private val assetsPdfScoresProviderFactory: AssetsPdfScoresProviderFactory,
    private val appsFinder: PublicPdfScoresAppsFinder,
    private val newThreadScheduler: Scheduler
) : PdfScoresRepository {

    private val allAssetsProviders: Flowable<List<AssetsPdfScoresProvider>> = appsFinder
        .pdfScoresApps
        .toFlowable(BackpressureStrategy.BUFFER)
        .map { apps ->
            apps.map { packageName ->
                appsFinder.getAssetCollectionFileUri(packageName)
            }
        }
        .flatMap { uris ->
            Flowable
                .fromIterable(uris)
                .map {
                    val assetFileDescriptor = appsFinder.openAssetCollectionRootFile(it)
                    assetsPdfScoresProviderFactory
                        .makeAssetsPdfScoresProvider(assetFileDescriptor)
                }
                .subscribeOn(newThreadScheduler)
                .toList()
                .toFlowable()
        }

    private val pdfScoresFromCombinedAssets: Flowable<List<PdfScore>>
        get() = allAssetsProviders
            .flatMap {
                Observable
                    .fromIterable(it)
                    .flatMap { it.pdfScores }
                    .toFlowable(BackpressureStrategy.BUFFER)
            }

    private val authorsFromCombinedAssets: Flowable<List<Author>>
        get() = allAssetsProviders
            .flatMap {
                Observable
                    .fromIterable(it)
                    .flatMap { it.authors }
                    .toFlowable(BackpressureStrategy.BUFFER)
            }

    override fun getScores(): Flowable<List<PdfScore>> = Flowable
        .combineLatest(
            arrayOf(
                pdfScoresDao.getAll(),
                pdfScoresFromCombinedAssets
            )
        ) { arrayOfListsOfScores ->
            val totalSum = arrayOfListsOfScores.map { (it as List<PdfScore>).size }.sum()
            ArrayList<PdfScore>(totalSum).apply {
                arrayOfListsOfScores.forEach {
                    this.addAll(it as List<PdfScore>)
                }
            }
        }

    override fun getAuthors(): Flowable<List<Author>> = Flowable
        .combineLatest(
            authorsDao.getAll(),
            authorsFromCombinedAssets,
            BiFunction<List<Author>, List<Author>, List<Author>> { authors1, authors2 ->
                ArrayList<Author>(authors1.size + authors2.size).apply {
                    addAll(authors1)
                    addAll(authors2)
                }
            }
        )
}