package com.lelloman.pdfscores.persistence.assets

import android.content.res.AssetFileDescriptor

interface AssetsPdfScoresProviderFactory {

    fun makeAssetsPdfScoresProvider(assetFileDescriptor: AssetFileDescriptor): AssetsPdfScoresProvider
}