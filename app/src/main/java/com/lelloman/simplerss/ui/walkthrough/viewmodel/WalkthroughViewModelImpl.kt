package com.lelloman.simplerss.ui.walkthrough.viewmodel

import android.os.Bundle
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lelloman.common.utils.LazyLiveData
import com.lelloman.common.utils.UrlValidator
import com.lelloman.common.view.AppTheme
import com.lelloman.common.viewmodel.command.CloseScreenCommand
import com.lelloman.common.viewmodel.command.SwipePageCommand
import com.lelloman.simplerss.R
import com.lelloman.simplerss.html.HtmlSpanner
import com.lelloman.simplerss.persistence.settings.AppSettings
import com.lelloman.simplerss.ui.OpenArticlesListScreenCommand
import com.lelloman.simplerss.ui.OpenFoundFeedListScreenCommand
import com.lelloman.simplerss.ui.common.repository.DiscoverRepository
import com.lelloman.simplerss.ui.walkthrough.ThemeListItem

class WalkthroughViewModelImpl(
    private val appSettings: AppSettings,
    private val discoveryRepository: DiscoverRepository,
    private val urlValidator: UrlValidator,
    private val htmlSpanner: HtmlSpanner,
    dependencies: Dependencies
) : WalkthroughViewModel(dependencies) {

    override val themes: MutableLiveData<List<ThemeListItem>> by LazyLiveData {
        subscription {
            appSettings
                .appTheme
                .subscribe { selectedTheme ->
                    themes.postValue(
                        AppTheme
                            .values()
                            .toList()
                            .mapIndexed { index, appTheme ->
                                ThemeListItem(
                                    id = index.toLong(),
                                    theme = appTheme,
                                    isEnabled = appTheme == selectedTheme
                                )
                            }
                    )
                }
        }
    }

    private val mutableFirstPageText: MutableLiveData<CharSequence> by LazyLiveData {
        val appName = getString(R.string.app_name)
        val text = getString(R.string.walkthrough_first_page, appName)
        val spannableText = htmlSpanner.fromHtml(text)
        mutableFirstPageText.postValue(spannableText)
    }
    override val firstPageText: LiveData<CharSequence> = mutableFirstPageText

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

    private val mutableNextButtonVisible: MutableLiveData<Boolean> by LazyLiveData {
        mutableNextButtonVisible.postValue(true)
    }
    override val nextButtonVisible: LiveData<Boolean> = mutableNextButtonVisible

    override fun onThemeClicked(theme: AppTheme) {
        appSettings.setAppTheme(theme)
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        super.onSaveInstanceState(bundle)
        bundle.putString(ARG_URL, discoverUrl.get())
    }

    override fun onRestoreInstanceState(bundle: Bundle) {
        super.onRestoreInstanceState(bundle)
        bundle.getString(ARG_URL)?.let { discoverUrl.set(it) }
    }

    override fun onCloseClicked() {
        appSettings.setShouldShowWalkthtough(false)
        emitCommand(OpenArticlesListScreenCommand)
        emitCommand(CloseScreenCommand)
    }

    override fun onKeyboardActionDone() {
        onDiscoverClicked()
    }

    override fun onDiscoverClicked() {
        urlValidator.maybePrependProtocol(discoverUrl.get())?.let { urlWithProtocol ->
            discoverUrl.set(urlWithProtocol)
            discoveryRepository.findFeeds(urlWithProtocol)
            emitCommand(OpenFoundFeedListScreenCommand(urlWithProtocol))
        }
    }

    override fun onMeteredConnectionNoClicked() {
        emitCommand(OpenArticlesListScreenCommand)
        emitCommand(CloseScreenCommand)
        appSettings.setUseMeteredNetwork(false)
        appSettings.setShouldShowWalkthtough(false)
    }

    override fun onMeteredConnectionYesClicked() {
        emitCommand(OpenArticlesListScreenCommand)
        emitCommand(CloseScreenCommand)
        appSettings.setUseMeteredNetwork(true)
        appSettings.setShouldShowWalkthtough(false)
    }

    override fun onNextButtonClicked() {
        emitCommand(SwipePageCommand(SwipePageCommand.Direction.RIGHT))
    }

    override fun onPageSelected(pageIndex: Int) {
        mutableNextButtonVisible.postValue(pageIndex < 4)
    }

    private companion object {
        const val ARG_URL = "Url"
    }
}