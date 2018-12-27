package com.lelloman.pdfscores.persistence.assets

import android.content.res.AssetFileDescriptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.subjects.SingleSubject

class AssetsCollectionProviderImpl(
    assetFileDescriptor: AssetFileDescriptor,
    ioScheduler: Scheduler,
    newThreadScheduler: Scheduler
) : AssetsCollectionProvider {

    private val collectionSubject = SingleSubject.create<AssetsCollection>()

    override val collection: Single<AssetsCollection> = collectionSubject.hide()

    init {
        Single
            .fromCallable {
                val jsonString = assetFileDescriptor
                    .createInputStream()
                    .bufferedReader()
                    .readText()
                GSON.fromJson(jsonString, AssetsCollection::class.java)
            }
            .subscribeOn(newThreadScheduler)
            .observeOn(ioScheduler)
            .doOnError {
                TODO("log this")
            }
            .onErrorReturn {
                AssetsCollection(
                    version = -1,
                    authors = emptyList(),
                    pdfScores = emptyList()
                )
            }
            .subscribe(collectionSubject)
    }

    private companion object {
        val GSON: Gson by lazy { GsonBuilder().create() }
    }
}