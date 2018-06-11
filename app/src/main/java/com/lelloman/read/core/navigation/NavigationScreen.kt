package com.lelloman.read.core.navigation

import com.lelloman.read.sources.view.AddSourceActivity
import com.lelloman.read.sources.view.SourcesListActivity
import kotlin.reflect.KFunction

enum class NavigationScreen(val starters: Array<KFunction<Unit>>) {
    SOURCES_LIST(arrayOf(SourcesListActivity.Companion::start)),
    ADD_SOURCE(arrayOf(AddSourceActivity.Companion::start));
}