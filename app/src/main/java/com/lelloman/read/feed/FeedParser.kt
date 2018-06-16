package com.lelloman.read.feed

import io.reactivex.Single
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader
import javax.inject.Inject

class FeedParser @Inject constructor() {

    @Throws(InvalidFeedTagException::class)
    fun parseFeeds(xml: String): Single<List<Feed>> = Single
        .fromCallable {
            val parser = XmlPullParserFactory.newInstance().newPullParser()

            val output = mutableListOf<Feed>()
            val inputStream = StringReader(xml)
            parser.setInput(inputStream)

            try {
                checkRootTag(parser)
            } catch (exception: InvalidFeedTagException) {
                throw exception
            } catch (exception: Exception) {
                throw MalformedXmlException(exception)
            }

            output
        }

    @Throws(InvalidFeedTagException::class)
    private fun checkRootTag(parser: XmlPullParser) {
        var startTag: String? = null
        while (parser.next() != XmlPullParser.END_TAG && startTag == null) {
            if (parser.eventType == XmlPullParser.START_TAG) {
                startTag = parser.name
            }
        }

        if (!VALID_ROOT_TAGS.contains(startTag)) {
            throw InvalidFeedTagException("Given xml has root tag <$startTag>, only " +
                "${VALID_ROOT_TAGS.map { "<$it>" }.joinToString(", ")} allowed")
        }
    }

    private companion object {
        val VALID_ROOT_TAGS = arrayOf("feed", "rss")
    }
}