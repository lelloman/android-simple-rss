package com.lelloman.read.ui.discover.viewmodel

import com.lelloman.read.R
import com.lelloman.read.core.view.ResourceProvider
import com.lelloman.read.core.viewmodel.BaseListItemViewModel
import com.lelloman.read.feed.finder.FoundFeed

class FoundFeedListItemViewModel(
    private val resourceProvider: ResourceProvider
) : BaseListItemViewModel<FoundFeed> {

    var feedName = ""
        private set

    var feedUrl = ""
        private set

    var feedUrlVisible = false
        private set

    var nArticles = ""
        private set

    override fun bind(item: FoundFeed) {
        feedUrlVisible = item.name != null
        feedName = item.name ?: item.url
        feedUrl = item.url
        nArticles = resourceProvider.getString(R.string.found_feed_n_articles, item.nArticles)
    }
}