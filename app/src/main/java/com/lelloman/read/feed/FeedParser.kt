package com.lelloman.read.feed

import io.reactivex.Single
import org.xmlpull.v1.XmlPullParserFactory
import javax.inject.Inject

class FeedParser @Inject constructor() {

    private val xmlParser = XmlPullParserFactory.newInstance()

    fun parseFeeds(xml: String): Single<List<Feed>> = Single.fromCallable {
        val output = mutableListOf<Feed>()

        output
    }
}