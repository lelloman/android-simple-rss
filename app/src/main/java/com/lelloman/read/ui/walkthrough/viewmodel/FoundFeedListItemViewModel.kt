package com.lelloman.read.ui.walkthrough.viewmodel

import com.lelloman.read.feed.finder.FoundFeed

class FoundFeedListItemViewModel {

    var feedName = ""
        private set

    var feedUrl = ""
        private set

    var feedUrlVisible = false
        private set

    fun bind(foundFeed: FoundFeed) {
        feedUrlVisible = foundFeed.name != null
        feedName = foundFeed.name ?: foundFeed.url
        feedUrl = foundFeed.url
    }
}