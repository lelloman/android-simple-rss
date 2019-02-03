package com.lelloman.simplerss.html

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import kotlin.reflect.KClass

class HtmlParserTest {

    private val tested = com.lelloman.simplerss.html.HtmlParser()

    @Test
    fun `strips link tags`() {
        val html = "click on <a href=\"\">the link</a> please"

        val (plainText, imagesUrls) = tested.parseTextAndImagesUrls(html)

        assertThat(plainText).isEqualTo("click on the link please")
        assertThat(imagesUrls).isEmpty()
    }

    @Test
    fun `strips div and nested link tags`() {
        val html = "click on <div><a href=\"\">the link</a> </div>please"

        val (plainText, imagesUrls) = tested.parseTextAndImagesUrls(html)

        assertThat(plainText).isEqualTo("click on the link please")
        assertThat(imagesUrls).isEmpty()
    }

    @Test
    fun `strips span and img tags`() {
        val html = "a<span> a <img src=\"www.asd.com\">meow</img></span>1234"

        val (plainText, imagesUrls) = tested.parseTextAndImagesUrls(html)

        assertThat(plainText).isEqualTo("a a meow1234")
        assertThat(imagesUrls).isEqualTo(listOf("www.asd.com"))
    }

    @Test
    fun `trims spaces at start and end and double spaces in between`() {
        val html = "        meow     <div><span>    1234 </span></div>5678       "

        val (plainText, imagesUrls) = tested.parseTextAndImagesUrls(html)

        assertThat(plainText).isEqualTo("meow 1234 5678")
        assertThat(imagesUrls).isEmpty()
    }

    @Test
    fun `parses simple html 1`() {
        val doc = tested.parseDoc(
            url = com.lelloman.simplerss.html.HtmlParserTest.Companion.URL_1,
            baseUrl = com.lelloman.simplerss.html.HtmlParserTest.Companion.URL_1,
            html = com.lelloman.simplerss.html.HtmlParserTest.Companion.SIMPLE_HTML_1
        )

        assertThat(doc.children).hasSize(1)
        doc.children[0].let { html ->
            assertThat(html.children).hasSize(2)
            html.children[0].let { head ->
                assertThat(head.children).hasSize(1)
                val link = head.children[0] as com.lelloman.simplerss.html.element.LinkDocElement
                assertThat(link.children).isEmpty()
                assertThat(link.linkType).isEqualTo("staceppa")
                assertThat(link.href).isEqualTo("www.staceppa.com")
            }
            html.children[1].let { body ->
                assertThat(body.children).hasSize(1)
                body.children[0].let { div ->
                    assertThat(div.children).hasSize(2)
                    div.children[0].let { it ->
                        assertThat(it).isInstanceOf(com.lelloman.simplerss.html.element.LinkDocElement::class.java)
                        (it as com.lelloman.simplerss.html.element.LinkDocElement).let { link ->
                            assertThat(link.linkType).isEqualTo("woof")
                            assertThat(link.href).isEqualTo("www.woof.com")
                        }
                    }
                    div.children[1].let {
                        assertThat(it).isInstanceOf(com.lelloman.simplerss.html.element.ADocElement::class.java)
                        (it as com.lelloman.simplerss.html.element.ADocElement).let { a ->
                            assertThat(a.children).hasSize(1)
                            assertThat(a.href).isEqualTo("www.woof.com")
                        }
                    }
                }
            }
        }
    }

    @Test
    fun `iterates over all elements in simple html 1`() {
        val doc = tested.parseDoc(
            url = com.lelloman.simplerss.html.HtmlParserTest.Companion.URL_1,
            baseUrl = com.lelloman.simplerss.html.HtmlParserTest.Companion.URL_1,
            html = com.lelloman.simplerss.html.HtmlParserTest.Companion.SIMPLE_HTML_1
        )
        val register = mutableMapOf<KClass<out com.lelloman.simplerss.html.element.DocElement>, Int>()

        doc.iterate {
            val clazz = it::class
            val count = register.getOrDefault(clazz, 0)
            register[clazz] = count + 1
        }

        assertThat(register).containsEntry(com.lelloman.simplerss.html.element.IrrelevantDocElement::class, 5)
        assertThat(register).containsEntry(com.lelloman.simplerss.html.element.LinkDocElement::class, 2)
        assertThat(register).containsEntry(com.lelloman.simplerss.html.element.ADocElement::class, 1)
    }

    private companion object {
        const val URL_1 = "http://www.staceppa.com"
        const val SIMPLE_HTML_1 = """
<html>
    <head>
        <link type="staceppa" href="www.staceppa.com" />
    </head>
    <body>
        <div>
            <link type="woof" href="www.woof.com" />
            <a href="www.woof.com"><img /></a>
        </div>
    </body>
</html>
        """
    }
}