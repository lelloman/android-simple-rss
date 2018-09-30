package com.lelloman.deviceinfo.testutils

import com.lelloman.testutils.TestApplicationRunner

@Suppress("unused")
class MockTestRunner : TestApplicationRunner() {
    override val testAppClassName: String = TestApp::class.java.name
}