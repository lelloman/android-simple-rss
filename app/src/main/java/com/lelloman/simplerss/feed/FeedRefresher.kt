package com.lelloman.simplerss.feed

import io.reactivex.Observable

interface FeedRefresher {

    val isLoading: Observable<Boolean>

    fun refresh()
}