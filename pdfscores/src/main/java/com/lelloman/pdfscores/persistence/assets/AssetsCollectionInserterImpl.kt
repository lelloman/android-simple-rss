package com.lelloman.pdfscores.persistence.assets

import com.lelloman.pdfscores.persistence.db.AuthorsDao
import com.lelloman.pdfscores.persistence.db.PdfScoresDao
import io.reactivex.Completable

class AssetsCollectionInserterImpl(
    private val assetsCollectionProvider: AssetsCollectionProvider,
    private val authorsDao: AuthorsDao,
    private val pdfScoresDao: PdfScoresDao
) : AssetsCollectionInserter {

    override fun insertAssetsCollectionIntoDb(): Completable = authorsDao
        .getAll()
        .firstOrError()
        .flatMapCompletable { authors ->
            if (authors.isEmpty()) {
                insertAssetsCollectionIntoDbActual()
            } else {
                Completable.complete()
            }
        }

    private fun insertAssetsCollectionIntoDbActual() = assetsCollectionProvider
        .collection
        .flatMapCompletable { assetCollection ->
            Completable.fromAction {
                authorsDao.insert(assetCollection.authors)
                pdfScoresDao.insert(assetCollection.pdfScores)
            }
        }
}