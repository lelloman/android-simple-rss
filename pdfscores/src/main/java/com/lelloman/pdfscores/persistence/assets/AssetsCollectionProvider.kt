package com.lelloman.pdfscores.persistence.assets

import io.reactivex.Single

interface AssetsCollectionProvider {

    val collection: Single<AssetsCollection>

    companion object {
        const val COLLECTION_JSON_FILE_NAME = "collection.json"
    }
}