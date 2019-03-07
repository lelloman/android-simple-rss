package com.lelloman.simplerss.testutils

import com.lelloman.common.androidtestutils.TestApplicationRunner

@Suppress("unused")
class MockTestRunner : TestApplicationRunner() {
    override val testAppClassName: String = TestApp::class.java.name
}