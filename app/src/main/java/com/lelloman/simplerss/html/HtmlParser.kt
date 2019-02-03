package com.lelloman.simplerss.html

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class HtmlParser {

    fun parseDoc(url: String,
                 baseUrl: String,
                 html: String): com.lelloman.simplerss.html.Doc {
        val jsoupDoc = Jsoup.parse(html)

        return com.lelloman.simplerss.html.Doc(url).apply {
            children = parseChildren(this, jsoupDoc.children())
        }
    }

    private fun parseChildren(parent: com.lelloman.simplerss.html.element.DocElement, elements: Elements): List<com.lelloman.simplerss.html.element.DocElement> =
        mutableListOf<com.lelloman.simplerss.html.element.DocElement>()
            .apply {
                addAll(elements.map { parseElement(parent, it) })
            }

    private fun parseElement(parent: com.lelloman.simplerss.html.element.DocElement, element: Element) =
        element
            .toDocElement(parent)
            .apply {
                children = parseChildren(this, element.children())
            }

    private fun Element.toDocElement(parent: com.lelloman.simplerss.html.element.DocElement) = tagName()
        .toLowerCase()
        .let { tag ->
            when (tag) {
                "a" -> com.lelloman.simplerss.html.element.ADocElement(
                    parent = parent,
                    href = attr("href")
                )
                "link" -> com.lelloman.simplerss.html.element.LinkDocElement(
                    parent = parent,
                    href = attr("href"),
                    linkType = attr("type")
                )
                else -> com.lelloman.simplerss.html.element.IrrelevantDocElement(
                    parent = parent,
                    tag = tag
                )
            }
        }

    fun parseTextAndImagesUrls(text: String): com.lelloman.simplerss.html.HtmlParser.TextAndImagesUrls {
        val doc = Jsoup.parse(text)
        val plainText = doc.text()
        val imagesUrls = mutableListOf<String>()

        doc.body().children().forEach { appendImgUrls(it, imagesUrls) }

        return com.lelloman.simplerss.html.HtmlParser.TextAndImagesUrls(
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