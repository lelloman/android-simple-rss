package com.lelloman.read.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class UrlValidatorTest {

    private val tested = UrlValidator()

    @Test
    fun `detects valid url 1`() {
        assertThat(tested.isValidUrl("staceppa.com")).isTrue()
    }

    @Test
    fun `detects valid url 2`() {
        assertThat(tested.isValidUrl("www.staceppa.com")).isTrue()
    }

    @Test
    fun `detects valid url 3`() {
        assertThat(tested.isValidUrl("staceppa.com/")).isTrue()
    }

    @Test
    fun `detects valid url 4`() {
        assertThat(tested.isValidUrl("nonnt.staceppa.com")).isTrue()
    }

    @Test
    fun `detects valid url 5`() {
        assertThat(tested.isValidUrl("ciccia.bebbe.staceppa.cc")).isTrue()
    }

    @Test
    fun `detects valid url 6`() {
        assertThat(tested.isValidUrl("www.nonnt.staceppa.alforno.it/klkl_123123-fdf/aff123123.xdfg")).isTrue()
    }

    @Test
    fun `detects valid url 7`() {
        assertThat(tested.isValidUrl("http://staceppa.com")).isTrue()
    }

    @Test
    fun `detects valid url 8`() {
        assertThat(tested.isValidUrl("https://staceppa.com")).isTrue()
    }


    @Test
    fun `detects invalid url 1`() {
        assertThat(tested.isValidUrl("/.staceppa.com")).isFalse()
    }

    @Test
    fun `detects invalid url 2`() {
        assertThat(tested.isValidUrl("htttp://www.staceppa.com")).isFalse()
    }

    @Test
    fun `detects invalid url 3`() {
        assertThat(tested.isValidUrl("-_-.staceppa.com/")).isFalse()
    }

    @Test
    fun `detects invalid url 4`() {
        assertThat(tested.isValidUrl("nonnt.staceppa.com/$$$")).isFalse()
    }

    @Test
    fun `detects invalid url 5`() {
        assertThat(tested.isValidUrl("ciccia.bebbe.staceppa.stodominiononesiste")).isFalse()
    }

    @Test
    fun `detects invalid url 6`() {
        assertThat(tested.isValidUrl("www.nonnt.it\\klkl_123123-fdf/aff123123.xdfg")).isFalse()
    }

    @Test
    fun `detects invalid url 7`() {
        assertThat(tested.isValidUrl("ssh://staceppa.com")).isFalse()
    }

    @Test
    fun `detects invalid url 8`() {
        assertThat(tested.isValidUrl("ftp://staceppa.com")).isFalse()
    }

    @Test
    fun `empty url is invalid`() {
        assertThat(tested.isValidUrl("")).isFalse()
    }

    @Test
    fun `null url is invalid`() {
        assertThat(tested.isValidUrl(null)).isFalse()
    }

    @Test
    fun `prepends http protocol`() {
        val original = "www.staceppa.com"

        val withProtocol = tested.maybePrependProtocol(original)

        assertThat(withProtocol).isEqualTo("http://$original")
    }

    @Test
    fun `does not prepend protocol if http already there`() {
        val original = "http://www.staceppa.com"

        val withProtocol = tested.maybePrependProtocol(original)

        assertThat(withProtocol).isEqualTo(original)
    }

    @Test
    fun `does not prepend protocol if https already there`() {
        val original = "https://www.staceppa.com"

        val withProtocol = tested.maybePrependProtocol(original)

        assertThat(withProtocol).isEqualTo(original)
    }
}