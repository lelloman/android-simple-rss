package com.lelloman.read.ui.sources.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import com.lelloman.read.R
import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.core.logger.LoggerFactory
import com.lelloman.read.feed.FeedFetcher
import com.lelloman.read.persistence.db.model.Source
import com.lelloman.read.ui.sources.repository.SourcesRepository
import com.lelloman.read.utils.LazyLiveData
import com.lelloman.read.utils.UrlValidator
import io.reactivex.Scheduler

class AddSourceViewModelImpl(
    resourceProvider: ResourceProvider,
    private val sourcesRepository: SourcesRepository,
    private val ioScheduler: Scheduler,
    private val uiScheduler: Scheduler,
    private val feedFetcher: FeedFetcher,
    loggerFactory: LoggerFactory,
    private val urlValidator: UrlValidator
) : AddSourceViewModel(resourceProvider) {

    override val sourceName = ObservableField<String>()
    override val sourceNameError = MutableLiveData<String>()

    override val sourceUrl = ObservableField<String>()
    override val sourceUrlError = MutableLiveData<String>()
    override val sourceUrlDrwable = MutableLiveData<Int>()

    private var saving = false

    private val logger = loggerFactory.getLogger(javaClass.simpleName)

    override val testingUrl: MutableLiveData<Boolean> by LazyLiveData {
        testingUrl.postValue(false)
    }

    init {
        sourceUrlDrwable.value = 0
    }

    override fun onTestUrlClicked() {
        if (testingUrl.value == true) return

        sourceUrlDrwable.value = 0

        val inputUrl = sourceUrl.get()

        if (!urlValidator.isValidUrl(inputUrl)) {
            sourceUrlError.value = getString(R.string.error_add_source_url)
        } else {
            val url = urlValidator.maybePrependProtocol(inputUrl)
            sourceUrl.set(url)
            sourceUrlError.value = ""
            testingUrl.value = true
            feedFetcher.testUrl(url!!)
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .doAfterTerminate { testingUrl.value = false }
                .subscribe({
                    logger.d("test result: $it")
                    if (it == FeedFetcher.TestResult.SUCCESS) {
                        sourceUrlDrwable.value = R.drawable.ic_check_green_24dp
                        sourceUrlError.value = ""
                    } else {
                        sourceUrlDrwable.value = 0
                        val errorId = when (it) {
                            FeedFetcher.TestResult.HTTP_ERROR -> R.string.test_feed_http_error
                            FeedFetcher.TestResult.XML_ERROR -> R.string.test_feed_xml_error
                            FeedFetcher.TestResult.EMPTY_SOURCE -> R.string.test_feed_empty_error
                            else -> R.string.something_went_wrong
                        }
                        sourceUrlError.value = getString(errorId)
                    }
                }, {
                    logger.w("Error while testing url $url", it)
                    shortToast(getString(R.string.something_went_wrong))
                })
        }
    }

    override fun onCloseClicked() = navigateBack()

    override fun onSaveClicked() {
        if (saving) return

        saving = true

        val name = sourceName.get()
        val inputUrl = sourceUrl.get()

        val hasValidName = name != null && name.length > 2
        val hasValidUrl = urlValidator.isValidUrl(inputUrl)

        if (hasValidName && hasValidUrl) {
            val url = urlValidator.maybePrependProtocol(inputUrl)
            sourceUrl.set(url)
            sourceNameError.value = ""
            sourceUrlError.value = ""
            sourcesRepository
                .insertSource(Source(
                    name = name!!,
                    url = url!!,
                    isActive = true
                ))
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .doAfterTerminate { saving = false }
                .subscribe({
                    navigateBack()
                }, {
                    shortToast(getString(R.string.something_went_wrong))
                })
        } else {
            val nameErrorValue = if (hasValidName) "" else getString(R.string.error_add_source_name)
            val urlErrorValue = if (hasValidUrl) "" else getString(R.string.error_add_source_url)

            sourceNameError.postValue(nameErrorValue)
            sourceUrlError.postValue(urlErrorValue)
        }
    }
}