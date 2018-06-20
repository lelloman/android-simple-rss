package com.lelloman.read.persistence.settings

import com.lelloman.read.utils.Constants.AppSettings.DEFAULT_MIN_SOURCE_REFRESH_INTERVAL

enum class SourceRefreshInterval(val ms: Long) {
    NEUROTIC(20_000L),
    VERY_FREQUENT(5 * 60_000L),
    FREQUENT(15 * 60_000L),
    NORMAL(2 * 60 * 60_000L),
    RELAXED(6 * 60 * 60_000L),
    STONER(12 * 60 * 60_000L);

    companion object {

        private val map = SourceRefreshInterval
            .values()
            .associateBy(SourceRefreshInterval::name)

        fun fromName(name: String): SourceRefreshInterval =
            map[name] ?: DEFAULT_MIN_SOURCE_REFRESH_INTERVAL
    }
}