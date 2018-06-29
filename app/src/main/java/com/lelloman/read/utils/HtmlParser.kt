package com.lelloman.read.utils

import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class HtmlParser {

    fun parseTextAndImagesUrls(text: String): Pair<String, List<String>> {
        val doc = Jsoup.parse(text)
        val plainText = doc.text()
        val imagesUrls = mutableListOf<String>()

        doc.body().children().forEach { appendImgUrls(it, imagesUrls) }

        return plainText to imagesUrls
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
}