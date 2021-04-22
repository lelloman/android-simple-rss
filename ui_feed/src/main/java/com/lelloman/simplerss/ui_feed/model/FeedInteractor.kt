package com.lelloman.simplerss.ui_feed.model

import io.reactivex.rxjava3.core.Single

interface FeedInteractor {

    fun loadFeed(): Single<List<FeedItem>>

    fun goToAbout()

    fun goToSettings()

    interface FeedItem {
        val id: Long
        val title: String
        val subtitle: String
        val content: String
        val link: String
        val imageUrl: String?
        val time: Long
        val sourceName: String
        val faviconId: String?
    }
}