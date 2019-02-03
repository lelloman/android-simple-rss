package com.lelloman.simplerss.ui.discover.viewmodel

import com.lelloman.common.view.ResourceProvider
import com.lelloman.common.viewmodel.BaseListItemViewModel
import com.lelloman.simplerss.R
import com.lelloman.simplerss.feed.finder.FoundFeed

class FoundFeedListItemViewModel(
    private val resourceProvider: ResourceProvider
) : BaseListItemViewModel<Long, FoundFeed> {

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