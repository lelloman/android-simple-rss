package com.lelloman.pdfscores.persistence.assets

import android.content.res.AssetFileDescriptor
import io.reactivex.Scheduler

class AssetsPdfScoresProviderFactoryImpl(
    private val ioScheduler: Scheduler
) : AssetsPdfScoresProviderFactory {

    override fun makeAssetsPdfScoresProvider(assetFileDescriptor: AssetFileDescriptor): AssetsPdfScoresProvider {
        return AssetsPdfScoresProviderImpl(
            assetFileDescriptor = assetFileDescriptor,
            ioScheduler = ioScheduler
        )
    }
}