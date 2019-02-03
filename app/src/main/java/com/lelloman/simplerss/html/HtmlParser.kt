package com.lelloman.simplerss.html

import com.lelloman.simplerss.html.element.ADocElement
import com.lelloman.simplerss.html.element.DocElement
import com.lelloman.simplerss.html.element.IrrelevantDocElement
import com.lelloman.simplerss.html.element.LinkDocElement
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class HtmlParser {

    fun parseDoc(url: String,
                 baseUrl: String,
                 html: String): Doc {
        val jsoupDoc = Jsoup.parse(html)

        return Doc(url).apply {
            children = parseChildren(this, jsoupDoc.children())
        }
    }

    private fun parseChildren(parent: DocElement, elements: Elements): List<DocElement> =
        mutableListOf<DocElement>()
            .apply {
                addAll(elements.map { parseElement(parent, it) })
            }

    private fun parseElement(parent: DocElement, element: Element) =
        element
            .toDocElement(parent)
            .apply {
                children = parseChildren(this, element.children())
            }

    private fun Element.toDocElement(parent: DocElement) = tagName()
        .toLowerCase()
        .let { tag ->
            when (tag) {
                "a" -> ADocElement(
                    parent = parent,
                    href = attr("href")
                )
                "link" -> LinkDocElement(
                    parent = parent,
                    href = attr("href"),
                    linkType = attr("type")
                )
                else -> IrrelevantDocElement(
                    parent = parent,
                    tag = tag
                )
            }
        }

    fun parseTextAndImagesUrls(text: String): TextAndImagesUrls {
        val doc = Jsoup.parse(text)
        val plainText = doc.text()
        val imagesUrls = mutableListOf<String>()

        doc.body().children().forEach { appendImgUrls(it, imagesUrls) }

        return TextAndImagesUrls(
            text = plainText,
            imagesUrls = imagesUrls
        )
    }

    private fun appendImgUrls(element: Element, urls: MutableList<String>) {
        if (element.tagName() == "img") {
            val url = element.attr("src")
            if (!url.isNullOrBlank()) {
                urls.add(url)
            }
        }

        element.children().forEach { appendImgUrls(it, urls) }
    }

    data class TextAndImagesUrls(
        val text: String,
        val imagesUrls: List<String>
    )
}