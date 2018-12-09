package com.lelloman.pdfscores.publicapi

import android.content.res.AssetFileDescriptor
import io.reactivex.Observable

interface PublicPdfScoresAppsFinder {

    val pdfScoresApps: Observable<List<String>>

    fun getAssetCollectionFileUri(packageName: String): String

    fun openAssetCollectionRootFile(uri: String): AssetFileDescriptor
}