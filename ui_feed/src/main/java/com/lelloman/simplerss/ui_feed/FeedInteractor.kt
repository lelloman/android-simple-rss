package com.lelloman.simplerss.ui_feed

import io.reactivex.rxjava3.core.Single

interface FeedInteractor {

    fun loadFeed() : Single<List<FeedItem>>

    fun goToAbout()

    fun goToSettings()

    interface FeedItem {
        val id: String
        val title: String
        val body: String
        val url: String
    }
}