package com.lelloman.read.feed

import com.lelloman.read.core.TimeProvider
import com.lelloman.read.feed.exception.InvalidFeedTagException
import com.lelloman.read.feed.exception.MalformedXmlException
import io.reactivex.Single
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.StringReader
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class FeedParser @Inject constructor(
    private val timeProvider: TimeProvider
) {
    private val pubDateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH)

    @Throws(InvalidFeedTagException::class)
    fun parseFeeds(xml: String): Single<List<ParsedFeed>> = Single
        .fromCallable {
            val parser = XmlPullParserFactory.newInstance().newPullParser()

            val output = mutableListOf<ParsedFeed>()
            val inputStream = StringReader(xml)
            parser.setInput(inputStream)

            try {
                checkRootTag(parser)
                findChannelTag(parser)
                while (parser.eventType != XmlPullParser.END_DOCUMENT) {
                    if (parser.eventType == XmlPullParser.START_TAG) {
                        if (parser.name == "item") {
                            val item = parserItemTag(parser)
                            item?.let { output.add(it) }
                        } else {
                            skip(parser)

                        }
                    } else {
                        parser.next()
                    }
                }
            } catch (exception: InvalidFeedTagException) {
                throw exception
            } catch (exception: Exception) {
                throw MalformedXmlException(exception)
            }

            output
        }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }

    private fun parserItemTag(parser: XmlPullParser): ParsedFeed? {
        var title: String? = null
        var link: String? = null
        var description: String? = null
        var pubDate: Date? = null

        while (parser.eventType != XmlPullParser.END_DOCUMENT) {
            if (parser.eventType == XmlPullParser.END_TAG && parser.name == "item") {
                break
            }

            if (parser.eventType == XmlPullParser.START_TAG) {
                when (parser.name) {
                    "title" -> title = readText(parser)
                    "pubDate" -> {
                        pubDate = try {
                            pubDateFormat.parse(readText(parser))
                        } catch (exception: Exception) {
                            null
                        }
                    }
                    "link" -> link = readText(parser)
                    "description" -> description = readText(parser)
                }
            }

            parser.next()
        }

        return if (title != null && link != null) {
            ParsedFeed(
                title = title,
                subtitle = description ?: "",
                link = link,
                timestamp = pubDate?.time ?: timeProvider.nowUtcMs()
            )
        } else {
            null
        }
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

    private fun findChannelTag(parser: XmlPullParser) {
        while (parser.eventType != XmlPullParser.END_DOCUMENT) {
            if (parser.eventType == XmlPullParser.START_TAG && parser.name == "channel") {
                parser.next()
                break
            }
            parser.next()
        }
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
                "${VALID_ROOT_TAGS.joinToString(", ") { "<$it>" }} are valid")
        }
    }

    private companion object {
        val VALID_ROOT_TAGS = arrayOf("feed", "rss")
    }
}