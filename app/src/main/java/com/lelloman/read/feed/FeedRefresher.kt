package com.lelloman.read.feed

import io.reactivex.Observable

interface FeedRefresher {

    val isLoading: Observable<Boolean>

    fun refresh()
}