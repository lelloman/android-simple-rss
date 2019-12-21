package com.lelloman.simplerss.ui

import com.lelloman.common.viewmodel.command.Command
import com.lelloman.simplerss.feed.finder.FoundFeed

object OpenSourcesListScreenCommand : Command

object OpenSettingsScreenCommand : Command

object OpenDiscoverScreenCommand : Command

object OpenDebugScreenCommand : Command

object OpenArticlesListScreenCommand : Command

object OpenResetSharedPreferencesScreenCommand : Command

object OpenClearDataConfirmationScreenCommand : Command

object OpenResetDbScreenCommand : Command

object OpenWalkthroughScreenCommand : Command

data class OpenArticleScreenCommand(val url: String) : Command

data class OpenFoundFeedListScreenCommand(val url: String) : Command

data class OpenAddSourceScreenCommand(val name: String? = null, val url: String? = null) : Command

data class OpenAddFoundFeedsConfirmationScreenCommand(val foundFeeds: ArrayList<FoundFeed>) :
    Command