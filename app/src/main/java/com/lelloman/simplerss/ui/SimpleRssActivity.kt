package com.lelloman.simplerss.ui

import androidx.databinding.ViewDataBinding
import com.lelloman.common.view.BaseActivity
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.common.viewmodel.command.Command
import com.lelloman.simplerss.ui.articles.view.ArticleActivity
import com.lelloman.simplerss.ui.articles.view.ArticlesListActivity
import com.lelloman.simplerss.ui.debug.view.DebugActivity
import com.lelloman.simplerss.ui.debug.view.ResetDbConfirmationDialogFragment
import com.lelloman.simplerss.ui.debug.view.ResetSharedPrefsConfirmationDialogFragment
import com.lelloman.simplerss.ui.discover.view.AddFoundFeedsConfirmationDialogFragment
import com.lelloman.simplerss.ui.discover.view.DiscoverUrlActivity
import com.lelloman.simplerss.ui.discover.view.FoundFeedListActivity
import com.lelloman.simplerss.ui.settings.view.ClearDataConfirmationDialogFragment
import com.lelloman.simplerss.ui.settings.view.SettingsActivity
import com.lelloman.simplerss.ui.sources.view.AddSourceActivity
import com.lelloman.simplerss.ui.sources.view.SourcesListActivity
import com.lelloman.simplerss.ui.walkthrough.view.WalkthroughActivity

abstract class SimpleRssActivity<VM : BaseViewModel, DB : ViewDataBinding> :
    BaseActivity<VM, DB>() {

    override fun onUnhandledCommand(command: Command) {
        when (command) {
            is OpenSourcesListScreenCommand -> SourcesListActivity.start(this)
            is OpenSettingsScreenCommand -> SettingsActivity.start(this)
            is OpenDiscoverScreenCommand -> DiscoverUrlActivity.start(this)
            is OpenDebugScreenCommand -> DebugActivity.start(this)
            is OpenArticlesListScreenCommand -> ArticlesListActivity.start(this)
            is OpenResetSharedPreferencesScreenCommand -> ResetSharedPrefsConfirmationDialogFragment()
                .show(supportFragmentManager, "reset prefs")

            is OpenClearDataConfirmationScreenCommand ->
                ClearDataConfirmationDialogFragment().show(supportFragmentManager, "clear data")
            is OpenResetDbScreenCommand -> ResetDbConfirmationDialogFragment()
                .show(supportFragmentManager, "reset db")
            is OpenWalkthroughScreenCommand -> WalkthroughActivity.start(this)
            is OpenArticleScreenCommand -> ArticleActivity.start(this, command.url)
            is OpenFoundFeedListScreenCommand -> FoundFeedListActivity.start(this, command.url)
            is OpenAddSourceScreenCommand -> AddSourceActivity.start(
                this,
                sourceName = command.name,
                sourceUrl = command.url
            )
            is OpenAddFoundFeedsConfirmationScreenCommand -> AddFoundFeedsConfirmationDialogFragment
                .newInstance(command.foundFeeds)
                .show(supportFragmentManager, "add found feeds")
        }
    }
}