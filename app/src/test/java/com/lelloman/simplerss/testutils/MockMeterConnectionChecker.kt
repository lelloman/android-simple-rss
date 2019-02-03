package com.lelloman.simplerss.testutils

import com.lelloman.common.view.MeteredConnectionChecker

class MockMeteredConnectionChecker(var isNetworkMeteredValue: Boolean = false) : MeteredConnectionChecker {

    override fun isNetworkMetered() = isNetworkMeteredValue
}