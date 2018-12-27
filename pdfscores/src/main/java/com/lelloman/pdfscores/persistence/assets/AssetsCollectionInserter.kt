package com.lelloman.pdfscores.persistence.assets

import io.reactivex.Completable

interface AssetsCollectionInserter {

    fun insertAssetsCollectionIntoDb(): Completable
}