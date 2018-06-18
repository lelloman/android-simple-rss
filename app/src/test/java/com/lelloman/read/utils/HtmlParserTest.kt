package com.lelloman.read.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class HtmlParserTest {

    private val tested = HtmlParser()

    @Test
    fun `strips link tags`() {
        val html = "click on <a href=\"\">the link</a> please"

        val (plainText, imagesUrls) = tested.withHtml(html)

        assertThat(plainText).isEqualTo("click on the link please")
        assertThat(imagesUrls).isEmpty()
    }

    @Test
    fun `strips div and nested link tags`() {
        val html = "click on <div><a href=\"\">the link</a> </div>please"

        val (plainText, imagesUrls) = tested.withHtml(html)

        assertThat(plainText).isEqualTo("click on the link please")
        assertThat(imagesUrls).isEmpty()
    }

    @Test
    fun `strips span and img tags`() {
        val html = "a<span> a <img src=\"www.asd.com\">meow</img></span>1234"

        val (plainText, imagesUrls) = tested.withHtml(html)

        assertThat(plainText).isEqualTo("a a meow1234")
        assertThat(imagesUrls).isEqualTo(listOf("www.asd.com"))
    }

    @Test
    fun `trims spaces at start and end and double spaces in between`() {
        val html = "        meow     <div><span>    1234 </span></div>5678       "

        val (plainText, imagesUrls) = tested.withHtml(html)

        assertThat(plainText).isEqualTo("meow 1234 5678")
        assertThat(imagesUrls).isEmpty()
    }
}