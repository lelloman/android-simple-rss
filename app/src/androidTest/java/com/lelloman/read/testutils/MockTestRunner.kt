package com.lelloman.read.testutils

import com.lelloman.testutils.TestApplicationRunner

class MockTestRunner : TestApplicationRunner() {
    override val testAppClassName: String = TestApp::class.java.name
}