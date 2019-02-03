package com.lelloman.simplerss.ui.walkthrough.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import android.os.Bundle
import android.view.View
import com.lelloman.common.navigation.DeepLink
import com.lelloman.common.utils.LazyLiveData
import com.lelloman.common.utils.UrlValidator
import com.lelloman.common.view.AppTheme
import com.lelloman.common.view.actionevent.SwipePageActionEvent

class WalkthroughViewModelImpl(
    private val appSettings: com.lelloman.simplerss.persistence.settings.AppSettings,
    private val discoveryRepository: com.lelloman.simplerss.ui.common.repository.DiscoverRepository,
    private val urlValidator: UrlValidator,
    dependencies: Dependencies
) : com.lelloman.simplerss.ui.walkthrough.viewmodel.WalkthroughViewModel(dependencies) {

    override val themes: MutableLiveData<List<com.lelloman.simplerss.ui.walkthrough.ThemeListItem>> by LazyLiveData {
        subscription {
            appSettings
                .appTheme
                .subscribe { selectedTheme ->
                    themes.postValue(
                        AppTheme
                            .values()
                            .toList()
                            .mapIndexed { index, appTheme ->
                                com.lelloman.simplerss.ui.walkthrough.ThemeListItem(
                                    id = index.toLong(),
                                    theme = appTheme,
                                    isEnabled = appTheme == selectedTheme
                                )
                            }
                    )
                }
        }
    }

    override val discoverUrl = ObservableField<String>()

    override val isFeedDiscoverLoading: MutableLiveData<Boolean> by LazyLiveData {
        subscription {
            discoveryRepository
                .isFindingFeeds
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe(isFeedDiscoverLoading::postValue)
        }
    }

    override fun onThemeClicked(theme: AppTheme) {
        appSettings.setAppTheme(theme)
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        super.onSaveInstanceState(bundle)
        bundle.putString(com.lelloman.simplerss.ui.walkthrough.viewmodel.WalkthroughViewModelImpl.Companion.ARG_URL, discoverUrl.get())
    }

    override fun onRestoreInstanceState(bundle: Bundle) {
        super.onRestoreInstanceState(bundle)
        bundle.getString(com.lelloman.simplerss.ui.walkthrough.viewmodel.WalkthroughViewModelImpl.Companion.ARG_URL)?.let { discoverUrl.set(it) }
    }

    override fun onCloseClicked(view: View) {
        appSettings.setShouldShowWalkthtough(false)
        navigateAndClose(com.lelloman.simplerss.navigation.SimpleRssNavigationScreen.ARTICLES_LIST)
    }

    override fun onKeyboardActionDone() {
        onDiscoverClicked(null)
    }

    override fun onDiscoverClicked(view: View?) {
        urlValidator.maybePrependProtocol(discoverUrl.get())?.let { urlWithProtocol ->
            discoverUrl.set(urlWithProtocol)
            discoveryRepository.findFeeds(urlWithProtocol)
            navigate(
                DeepLink(com.lelloman.simplerss.navigation.SimpleRssNavigationScreen.FOUND_FEED_LIST)
                    .putString(com.lelloman.simplerss.ui.walkthrough.viewmodel.WalkthroughViewModelImpl.Companion.ARG_URL, urlWithProtocol)
            )
        }
    }

    override fun onMeteredConnectionNoClicked(view: View) {
        navigateAndClose(com.lelloman.simplerss.navigation.SimpleRssNavigationScreen.ARTICLES_LIST)
        appSettings.setUseMeteredNetwork(false)
        appSettings.setShouldShowWalkthtough(false)
    }

    override fun onMeteredConnectionYesClicked(view: View) {
        navigateAndClose(com.lelloman.simplerss.navigation.SimpleRssNavigationScreen.ARTICLES_LIST)
        appSettings.setUseMeteredNetwork(true)
        appSettings.setShouldShowWalkthtough(false)
    }

    override fun onFirstPageOkClicked(view: View) {
        emitViewActionEvent(SwipePageActionEvent(SwipePageActionEvent.Direction.RIGHT))
    }

    private companion object {
        const val ARG_URL = "Url"
    }
}