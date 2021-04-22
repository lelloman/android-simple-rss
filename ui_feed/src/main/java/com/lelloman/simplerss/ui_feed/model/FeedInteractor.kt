package com.lelloman.simplerss.ui_feed.model

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface FeedInteractor {

    fun loadFeed(): Single<List<FeedItem>>

    fun refreshFeed(): Completable

    fun goToAbout()

    fun goToSettings()

    interface FeedItem {
        val id: String
        val title: String
        val body: String
        val url: String
    }
}