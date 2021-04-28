package com.lelloman.simplerss.domain_local_sources.internal

import com.lelloman.simplerss.domain_feed.FeedSource
import com.lelloman.simplerss.domain_local_sources.R

object LocalSourceType : FeedSource.Type {
    override val nameStringId = R.string.local_source_type_name
}