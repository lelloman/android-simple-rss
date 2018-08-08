package com.lelloman.read.feed.fetcher

sealed class TestResult {
    override fun toString(): String = javaClass.simpleName
}

object HttpError : TestResult()
object XmlError : TestResult()
object EmptySource : TestResult()
object UnknownError : TestResult()
class Success(val nArticles: Int) : TestResult()