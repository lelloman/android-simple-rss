package com.lelloman.simplerss.persistence.settings

import com.lelloman.common.utils.model.Named
import com.lelloman.simplerss.persistence.settings.AppSettings.Companion.DEFAULT_MIN_SOURCE_REFRESH_INTERVAL

enum class SourceRefreshInterval(val ms: Long) : Named {
    NEUROTIC(60_000L),
    VERY_FREQUENT(5 * 60_000L),
    FREQUENT(15 * 60_000L),
    NORMAL(2 * 60 * 60_000L),
    RELAXED(6 * 60 * 60_000L),
    STONER(12 * 60 * 60_000L);

    companion object {

        private val namesMap = SourceRefreshInterval
            .values()
            .associateBy(SourceRefreshInterval::name)

        fun fromName(name: String): SourceRefreshInterval = if (namesMap.containsKey(name)) {
            namesMap[name]!!
        } else {
            DEFAULT_MIN_SOURCE_REFRESH_INTERVAL
        }
    }
}
