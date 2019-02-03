package com.lelloman.simplerss.feed.fetcher

sealed class TestResult {
    override fun toString(): String = javaClass.simpleName
}

object HttpError : com.lelloman.simplerss.feed.fetcher.TestResult()
object XmlError : com.lelloman.simplerss.feed.fetcher.TestResult()
object EmptySource : com.lelloman.simplerss.feed.fetcher.TestResult()
object UnknownError : com.lelloman.simplerss.feed.fetcher.TestResult()
class Success(val nArticles: Int, val title: String?) : com.lelloman.simplerss.feed.fetcher.TestResult()