package com.lelloman.read.feed

import io.reactivex.Observable

interface FeedManager {

    val isLoading: Observable<Boolean>

    fun refresh()
}