package com.lelloman.simplerss.feed

class ParsedFeeds(private val parsedFeeds: MutableList<com.lelloman.simplerss.feed.ParsedFeed> = ArrayList())
    : MutableList<com.lelloman.simplerss.feed.ParsedFeed> by parsedFeeds {
    var title: String? = null
}