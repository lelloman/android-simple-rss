package com.lelloman.simplerss.ui_source

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import io.reactivex.rxjava3.core.Observable

interface SourcesInteractor {

    fun observeSources(): Observable<List<Source>>

    fun observeSourceTypes(): Observable<Set<SourceType>>

    data class Source(
        val id: String,
        val name: String,
        val sourceType: SourceType
    )

    data class SourceType(
        @DrawableRes val iconResId: Int,
        @StringRes val nameResId: Int
    )
}