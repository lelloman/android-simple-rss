package com.lelloman.read.feed

class ParsedFeeds(private val parsedFeeds: MutableList<ParsedFeed> = ArrayList())
    : MutableList<ParsedFeed> by parsedFeeds {
    var title: String? = null
}