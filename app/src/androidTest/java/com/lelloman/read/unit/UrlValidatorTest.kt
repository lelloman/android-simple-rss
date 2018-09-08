package com.lelloman.read.unit

import com.google.common.truth.Truth
import com.lelloman.read.utils.UrlValidator
import org.junit.Test

class UrlValidatorTest {

    private val tested = UrlValidator()

    @Test
    fun detectsValidUrl1() {
        Truth.assertThat(tested.isValidUrl("staceppa.com")).isTrue()
    }

    @Test
    fun detectsValidUrl2() {
        Truth.assertThat(tested.isValidUrl("www.staceppa.com")).isTrue()
    }

    @Test
    fun detectsValidUrl3() {
        Truth.assertThat(tested.isValidUrl("staceppa.com/")).isTrue()
    }

    @Test
    fun detectsValidUrl4() {
        Truth.assertThat(tested.isValidUrl("nonnt.staceppa.com")).isTrue()
    }

    @Test
    fun detectsValidUrl5() {
        Truth.assertThat(tested.isValidUrl("ciccia.bebbe.staceppa.cc")).isTrue()
    }

    @Test
    fun detectsValidUrl6() {
        Truth.assertThat(tested.isValidUrl("www.nonnt.staceppa.alforno.it/klkl_123123-fdf/aff123123.xdfg")).isTrue()
    }

    @Test
    fun detectsValidUrl7() {
        Truth.assertThat(tested.isValidUrl("http://staceppa.com")).isTrue()
    }

    @Test
    fun detectsValidUrl8() {
        Truth.assertThat(tested.isValidUrl("https://staceppa.com")).isTrue()
    }


    @Test
    fun detectsInvalidUrl1() {
        Truth.assertThat(tested.isValidUrl("/.staceppa.com")).isFalse()
    }

    @Test
    fun detectsInvalidUrl2() {
        Truth.assertThat(tested.isValidUrl("htttp://www.staceppa.com")).isFalse()
    }

    @Test
    fun detectsInvalidUrl3() {
        Truth.assertThat(tested.isValidUrl("-_-.staceppa.com/")).isFalse()
    }

    @Test
    fun detectsInvalidUrl4() {
        Truth.assertThat(tested.isValidUrl("nonnt.staceppa.com/ $$$ ")).isFalse()
    }

    @Test
    fun detectsInvalidUrl5() {
        Truth.assertThat(tested.isValidUrl("ciccia$.bebbe.staceppa.stodominiononesiste")).isFalse()
    }

    @Test
    fun detectsInvalidUrl6() {
        Truth.assertThat(tested.isValidUrl("www.nonnt.it\\klkl_123123-fdf/aff123123.xdfg")).isFalse()
    }

    @Test
    fun detectsInvalidUrl7() {
        Truth.assertThat(tested.isValidUrl("ssh://staceppa.com")).isFalse()
    }

    @Test
    fun detectsInvalidUrl8() {
        Truth.assertThat(tested.isValidUrl("ftp://staceppa.com")).isFalse()
    }

    @Test
    fun emptyUrlIsInvalid() {
        Truth.assertThat(tested.isValidUrl("")).isFalse()
    }

    @Test
    fun nullUrlIsInvalid() {
        Truth.assertThat(tested.isValidUrl(null)).isFalse()
    }

    @Test
    fun prependsHttpProtocol() {
        val original = "www.staceppa.com"

        val withProtocol = tested.maybePrependProtocol(original)

        Truth.assertThat(withProtocol).isEqualTo("http://$original")
    }

    @Test
    fun doesNotPrependProtocolIfHttpAlreadyThere() {
        val original = "http://www.staceppa.com"

        val withProtocol = tested.maybePrependProtocol(original)

        Truth.assertThat(withProtocol).isEqualTo(original)
    }

    @Test
    fun doesNotPrependProtocolIfHttpsAlreadyThere() {
        val original = "https://www.staceppa.com"

        val withProtocol = tested.maybePrependProtocol(original)

        Truth.assertThat(withProtocol).isEqualTo(original)
    }

    @Test
    fun findBaseUrl1WithoutProtocol() {
        val url = "http://www.staceppa.com"

        val tester = tested.findBaseUrlWithoutProtocol(url).test()

        tester.assertValues("www.staceppa.com")
    }

    @Test
    fun findBaseUrl2WithoutProtocol() {
        val url = "https://www.staceppa.com/asdasdasd/12345"

        val tester = tested.findBaseUrlWithoutProtocol(url).test()

        tester.assertValues("www.staceppa.com")
    }

    @Test
    fun findBaseUrl3WithoutProtocol() {
        val url = "http://www.staceppa.com/-_-/-_-/"

        val tester = tested.findBaseUrlWithoutProtocol(url).test()

        tester.assertValues("www.staceppa.com")
    }

    @Test
    fun doesNotFindUrlWithMissingProtocol() {
        val url = "www.staceppa.com"

        val tester = tested.findBaseUrlWithoutProtocol(url).test()

        tester.assertValues()
        tester.assertComplete()
    }

    @Test
    fun doesNotFindUrlWithWeirdProtocol1() {
        val url = "theweirdprotocol://www.staceppa.com"

        val tester = tested.findBaseUrlWithoutProtocol(url).test()

        tester.assertValues()
        tester.assertComplete()
    }


    @Test
    fun findBaseUrl1WithProtocol() {
        val url = "http://www.staceppa.com"

        val tester = tested.findBaseUrlWithProtocol(url).test()

        tester.assertValues("http://www.staceppa.com")
    }

    @Test
    fun findBaseUrl2WithProtocol() {
        val url = "https://www.staceppa.com/asdasdasd/12345"

        val tester = tested.findBaseUrlWithProtocol(url).test()

        tester.assertValues("https://www.staceppa.com")
    }

    @Test
    fun findBaseUrl3WithProtocol() {
        val url = "http://www.staceppa.com/-_-/-_-/"

        val tester = tested.findBaseUrlWithProtocol(url).test()

        tester.assertValues("http://www.staceppa.com")
    }

    @Test
    fun prependsBaseUrl() {
        val baseUrl = "www.asd.com"
        val path = "/sta/ceppa"

        val result = tested.maybePrependBaseUrl(
            baseUrl = baseUrl,
            path = path
        )

        Truth.assertThat(result).isEqualTo("www.asd.com/sta/ceppa")
    }

    @Test
    fun doesNotPrependBaseUrl() {
        val baseUrl = "www.asd.com"
        val path = "www.staceppa.com/sta/ceppa"

        val result = tested.maybePrependBaseUrl(
            baseUrl = baseUrl,
            path = path
        )

        Truth.assertThat(result).isEqualTo("www.staceppa.com/sta/ceppa")
    }
}