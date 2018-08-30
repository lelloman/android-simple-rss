package com.lelloman.read.ui.discover.viewmodel

import android.view.View
import com.lelloman.read.R
import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.feed.finder.FoundFeed

class FoundFeedListItemViewModel(
    private val resourceProvider: ResourceProvider
) {

    var feedName = ""
        private set

    var feedUrl = ""
        private set

    var feedUrlVisible = false
        private set

    var nArticles = ""
        private set

    fun bind(foundFeed: FoundFeed) {
        feedUrlVisible = foundFeed.name != null
        feedName = foundFeed.name ?: foundFeed.url
        feedUrl = foundFeed.url
        nArticles = resourceProvider.getString(R.string.found_feed_n_articles, foundFeed.nArticles)
    }

    fun onEditClicked(view: View?){
        
    }

    fun onAddClicked(view: View?){

    }
}