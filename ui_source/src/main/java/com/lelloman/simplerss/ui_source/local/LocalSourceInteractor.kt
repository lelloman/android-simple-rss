package com.lelloman.simplerss.ui_source.local

import io.reactivex.rxjava3.core.Completable

interface LocalSourceInteractor {

    fun addLocalSource(name: String, url: String): Completable

}