package com.lelloman.simplerss.ui.sources.viewmodel

import com.google.common.truth.Truth.assertThat
import com.lelloman.common.jvmtestutils.AndroidArchTest
import com.lelloman.common.jvmtestutils.MockLoggerFactory
import com.lelloman.common.jvmtestutils.MockResourceProvider
import com.lelloman.common.navigation.CloseScreenNavigationEvent
import com.lelloman.common.utils.UrlValidator
import com.lelloman.common.view.actionevent.ToastEvent
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.simplerss.R
import com.lelloman.simplerss.feed.fetcher.EmptySource
import com.lelloman.simplerss.feed.fetcher.FeedFetcher
import com.lelloman.simplerss.feed.fetcher.HttpError
import com.lelloman.simplerss.feed.fetcher.Success
import com.lelloman.simplerss.feed.fetcher.TestResult
import com.lelloman.simplerss.feed.fetcher.UnknownError
import com.lelloman.simplerss.feed.fetcher.XmlError
import com.lelloman.simplerss.persistence.db.model.Source
import com.lelloman.simplerss.ui.common.repository.SourcesRepository
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
import org.junit.Test

class AddSourceViewModelImplTest : AndroidArchTest() {

    private val sourcesRepository: SourcesRepository = mock()
    private val feedFetcher: FeedFetcher = mock()
    private val urlValidator: UrlValidator = mock()

    private lateinit var tested: AddSourceViewModelImpl

    override fun setUp() {
        tested = AddSourceViewModelImpl(
            dependencies = BaseViewModel.Dependencies(
                resourceProvider = MockResourceProvider(),
                actionTokenProvider = mock(),
                settings = mock(),
                uiScheduler = trampoline(),
                ioScheduler = trampoline(),
                loggerFactory = MockLoggerFactory()
            ),
            sourcesRepository = sourcesRepository,
            feedFetcher = feedFetcher,
            urlValidator = urlValidator
        )
    }

    @Test
    fun `source url drawable is not set upon instantiation`() {
        assertThat(tested.sourceUrlDrawable.value).isEqualTo(0)
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
        givenFeedFetcherTestsUrl(EmptySource)

        tested.onTestUrlClicked()

        verify(urlValidator).isValidUrl(originalUrl)
        assertThat(tested.sourceUrl.get()).isEqualTo(urlWithProtocol)
    }

    @Test
    fun `if it is testing url does not triget url testing again`() {
        givenHasValidUrlSet()
        val feedFetcherSubject = SingleSubject.create<TestResult>()
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
        whenever(feedFetcher.testUrl(any())).thenReturn(Single.error<TestResult>(Exception()))
        givenHasValidUrlSet()

        tested.onTestUrlClicked()

        actionEventsObserver.assertValues(ToastEvent("${R.string.something_went_wrong}"))
        assertThat(tested.testingUrl.value).isFalse()
    }

    @Test
    fun `shows green check and hides error message if feed fetcher test is successful`() {
        givenHasValidUrlSet()
        givenFeedFetcherTestsUrl(Success(0, null))

        tested.onTestUrlClicked()

        tested.apply {
            assertThat(testingUrl.value).isFalse()
            assertThat(sourceUrlDrawable.value).isEqualTo(R.drawable.ic_check_green_24dp)
            assertThat(sourceUrlError.value).isEmpty()
        }
    }

    @Test
    fun `shows error and hides green check if feed fetcher test result is http error`() {
        givenHasValidUrlSet()
        givenFeedFetcherTestsUrl(HttpError)

        tested.onTestUrlClicked()

        tested.apply {
            assertThat(sourceUrlDrawable.value).isEqualTo(0)
            assertThat(sourceUrlError.value).isEqualTo("${R.string.test_feed_http_error}")
            assertThat(testingUrl.value).isFalse()
        }
    }

    @Test
    fun `shows error and hides green check if feed fetcher test result is empty source`() {
        givenHasValidUrlSet()
        givenFeedFetcherTestsUrl(EmptySource)

        tested.onTestUrlClicked()

        tested.apply {
            assertThat(sourceUrlDrawable.value).isEqualTo(0)
            assertThat(sourceUrlError.value).isEqualTo("${R.string.test_feed_empty_error}")
            assertThat(testingUrl.value).isFalse()
        }
    }

    @Test
    fun `shows error and hides green check if feed fetcher test result is xml error`() {
        givenHasValidUrlSet()
        givenFeedFetcherTestsUrl(XmlError)

        tested.onTestUrlClicked()

        tested.apply {
            assertThat(sourceUrlDrawable.value).isEqualTo(0)
            assertThat(sourceUrlError.value).isEqualTo("${R.string.test_feed_xml_error}")
            assertThat(testingUrl.value).isFalse()
        }
    }

    @Test
    fun `shows error and hides green check if feed fetcher test result is unknown error`() {
        givenHasValidUrlSet()
        givenFeedFetcherTestsUrl(UnknownError)

        tested.onTestUrlClicked()

        tested.apply {
            assertThat(sourceUrlDrawable.value).isEqualTo(0)
            assertThat(sourceUrlError.value).isEqualTo("${R.string.something_went_wrong}")
            assertThat(testingUrl.value).isFalse()
        }
    }

    @Test
    fun `navigates back when click close`() {
        val viewActionEventObserver = tested.viewActionEvents.test()

        tested.onCloseClicked()

        viewActionEventObserver.assertValues(CloseScreenNavigationEvent)
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
        viewActionEventObserver.assertValues(CloseScreenNavigationEvent)
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

    private fun givenFeedFetcherTestsUrl(testResult: TestResult) {
        whenever(feedFetcher.testUrl(any())).thenReturn(Single.just(testResult))
    }
}