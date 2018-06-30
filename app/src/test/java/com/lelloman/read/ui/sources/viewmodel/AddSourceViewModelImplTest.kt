package com.lelloman.read.ui.sources.viewmodel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.lelloman.read.R
import com.lelloman.read.core.navigation.BackNavigationEvent
import com.lelloman.read.core.view.ToastEvent
import com.lelloman.read.feed.FeedFetcher
import com.lelloman.read.persistence.db.model.Source
import com.lelloman.read.testutils.MockLoggerFactory
import com.lelloman.read.testutils.MockResourceProvider
import com.lelloman.read.testutils.test
import com.lelloman.read.ui.sources.repository.SourcesRepository
import com.lelloman.read.utils.UrlValidator
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.anyOrNull
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers.trampoline
import io.reactivex.subjects.SingleSubject
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class AddSourceViewModelImplTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val sourcesRepository: SourcesRepository = mock()
    private val feedFetcher: FeedFetcher = mock()
    private val urlValidator: UrlValidator = mock()

    private lateinit var tested: AddSourceViewModelImpl

    @Before
    fun setUp() {
        tested = AddSourceViewModelImpl(
            resourceProvider = MockResourceProvider(),
            sourcesRepository = sourcesRepository,
            uiScheduler = trampoline(),
            ioScheduler = trampoline(),
            feedFetcher = feedFetcher,
            loggerFactory = MockLoggerFactory(),
            urlValidator = urlValidator
        )
    }

    @Test
    fun `source url drawable is not set upon instantiation`() {
        assertThat(tested.sourceUrlDrwable.value).isEqualTo(0)
    }

    @Test
    fun `shows error if test url button is clicked and url is not valid`() {
        givenUrlIsNotValid()
        assertThat(tested.sourceUrlError.value).isNull()

        tested.onTestUrlClicked()

        assertThat(tested.sourceUrlError.value).isEqualTo("${R.string.error_add_source_url}")
    }

    @Test
    fun `set url with maybe protocol when test url button is clicked and url is valid`() {
        val originalUrl = "original url"
        val urlWithProtocol = "url with protocol"
        givenHasValidUrlSet(originalUrl, urlWithProtocol)
        givenFeedFetcherTestsUrl(FeedFetcher.TestResult.EMPTY_SOURCE)

        tested.onTestUrlClicked()

        verify(urlValidator).isValidUrl(originalUrl)
        assertThat(tested.sourceUrl.get()).isEqualTo(urlWithProtocol)
    }

    @Test
    fun `if it is testing url does not triget url testing again`() {
        givenHasValidUrlSet()
        val feedFetcherSubject = SingleSubject.create<FeedFetcher.TestResult>()
        whenever(feedFetcher.testUrl(any())).thenReturn(feedFetcherSubject)
        tested.onTestUrlClicked()
        assertThat(tested.testingUrl.value).isTrue()
        reset(feedFetcher, urlValidator)

        tested.onTestUrlClicked()

        verifyZeroInteractions(feedFetcher, urlValidator)
    }

    @Test
    fun `if feed fetcher throws an error shows toast`() {
        val actionEventsObserver = tested.viewActionEvents.test()
        whenever(feedFetcher.testUrl(any())).thenReturn(Single.error<FeedFetcher.TestResult>(Exception()))
        givenHasValidUrlSet()

        tested.onTestUrlClicked()

        actionEventsObserver.assertValues(ToastEvent("${R.string.something_went_wrong}"))
        assertThat(tested.testingUrl.value).isFalse()
    }

    @Test
    fun `shows green check and hides error message if feed fetcher test is successful`() {
        givenHasValidUrlSet()
        givenFeedFetcherTestsUrl(FeedFetcher.TestResult.SUCCESS)

        tested.onTestUrlClicked()

        tested.apply {
            assertThat(testingUrl.value).isFalse()
            assertThat(sourceUrlDrwable.value).isEqualTo(R.drawable.ic_check_green_24dp)
            assertThat(sourceUrlError.value).isEmpty()
        }
    }

    @Test
    fun `shows error and hides green check if feed fetcher test result is http error`() {
        givenHasValidUrlSet()
        givenFeedFetcherTestsUrl(FeedFetcher.TestResult.HTTP_ERROR)

        tested.onTestUrlClicked()

        tested.apply {
            assertThat(sourceUrlDrwable.value).isEqualTo(0)
            assertThat(sourceUrlError.value).isEqualTo("${R.string.test_feed_http_error}")
            assertThat(testingUrl.value).isFalse()
        }
    }

    @Test
    fun `shows error and hides green check if feed fetcher test result is empty source`() {
        givenHasValidUrlSet()
        givenFeedFetcherTestsUrl(FeedFetcher.TestResult.EMPTY_SOURCE)

        tested.onTestUrlClicked()

        tested.apply {
            assertThat(sourceUrlDrwable.value).isEqualTo(0)
            assertThat(sourceUrlError.value).isEqualTo("${R.string.test_feed_empty_error}")
            assertThat(testingUrl.value).isFalse()
        }
    }

    @Test
    fun `shows error and hides green check if feed fetcher test result is xml error`() {
        givenHasValidUrlSet()
        givenFeedFetcherTestsUrl(FeedFetcher.TestResult.XML_ERROR)

        tested.onTestUrlClicked()

        tested.apply {
            assertThat(sourceUrlDrwable.value).isEqualTo(0)
            assertThat(sourceUrlError.value).isEqualTo("${R.string.test_feed_xml_error}")
            assertThat(testingUrl.value).isFalse()
        }
    }

    @Test
    fun `shows error and hides green check if feed fetcher test result is unknown error`() {
        givenHasValidUrlSet()
        givenFeedFetcherTestsUrl(FeedFetcher.TestResult.UNKNOWN_ERROR)

        tested.onTestUrlClicked()

        tested.apply {
            assertThat(sourceUrlDrwable.value).isEqualTo(0)
            assertThat(sourceUrlError.value).isEqualTo("${R.string.something_went_wrong}")
            assertThat(testingUrl.value).isFalse()
        }
    }

    @Test
    fun `navigates back when click close`() {
        val viewActionEventObserver = tested.viewActionEvents.test()

        tested.onCloseClicked()

        viewActionEventObserver.assertValues(BackNavigationEvent)
    }

    @Test
    fun `shows name error when click on save and name is not valid`() {
        givenHasValidUrlSet()
        tested.sourceName.set("")

        tested.onSaveClicked()

        tested.apply {
            assertThat(sourceNameError.value).isEqualTo("${R.string.error_add_source_name}")
            assertThat(sourceUrlError.value).isEqualTo("")
        }
    }

    @Test
    fun `shows url error when click on save and url is not valid`() {
        givenUrlIsNotValid()
        tested.sourceName.set("a valid name")

        tested.onSaveClicked()

        tested.apply {
            assertThat(sourceNameError.value).isEqualTo("")
            assertThat(sourceUrlError.value).isEqualTo("${R.string.error_add_source_url}")
        }
    }

    @Test
    fun `shows both url and name error when click on save and both url and name are not valid`() {
        givenUrlIsNotValid()
        tested.sourceName.set("a")

        tested.onSaveClicked()

        tested.apply {
            assertThat(sourceNameError.value).isEqualTo("${R.string.error_add_source_name}")
            assertThat(sourceUrlError.value).isEqualTo("${R.string.error_add_source_url}")
        }
    }

    @Test
    fun `insert source into repository and navigates back when click on save and both name and url are valid`() {
        val viewActionEventObserver = tested.viewActionEvents.test()
        val originalUrl = "not the url to be saved"
        val url = "the url to be saved"
        val name = "the name of the source"
        tested.sourceName.set(name)
        givenHasValidUrlSet(originalUrl = originalUrl, urlWithProtocol = url)
        givenCanInsertSource()

        tested.onSaveClicked()

        verify(sourcesRepository).insertSource(Source(
            name = name,
            url = url,
            isActive = true
        ))
        viewActionEventObserver.assertValues(BackNavigationEvent)
        tested.apply {
            assertThat(sourceNameError.value).isEmpty()
            assertThat(sourceUrlError.value).isEmpty()
            assertThat(sourceUrl.get()).isEqualTo(url)
        }
    }

    @Test
    fun `does nothing when save is clicked but it is already saving`() {
        tested.sourceName.set("the name@")
        givenHasValidUrlSet()
        whenever(sourcesRepository.insertSource(any())).thenReturn(Single.never())
        tested.onSaveClicked()
        verify(sourcesRepository).insertSource(any())
        reset(sourcesRepository, urlValidator)

        tested.onSaveClicked()

        verifyZeroInteractions(sourcesRepository, urlValidator)
    }

    @Test
    fun `does not navigate back and shows toast if insert source fails`() {
        val viewActionEventObserver = tested.viewActionEvents.test()
        tested.sourceName.set("the name")
        givenHasValidUrlSet()
        whenever(sourcesRepository.insertSource(any())).thenReturn(Single.error(Exception()))

        tested.onSaveClicked()

        viewActionEventObserver.assertValues(ToastEvent("${R.string.something_went_wrong}"))

    }

    private fun givenCanInsertSource() {
        whenever(sourcesRepository.insertSource(any())).thenReturn(Single.just(0))
    }

    private fun givenHasValidUrlSet(originalUrl: String = "original url", urlWithProtocol: String = "url with protocol") {
        whenever(urlValidator.maybePrependProtocol(originalUrl)).thenReturn(urlWithProtocol)
        tested.sourceUrl.set(originalUrl)
        givenUrlIsValid()
    }

    private fun givenUrlIsValid() {
        whenever(urlValidator.isValidUrl(anyOrNull())).thenReturn(true)
    }

    private fun givenUrlIsNotValid() {
        whenever(urlValidator.isValidUrl(anyOrNull())).thenReturn(false)
    }

    private fun givenFeedFetcherTestsUrl(testResult: FeedFetcher.TestResult) {
        whenever(feedFetcher.testUrl(any())).thenReturn(Single.just(testResult))
    }
}