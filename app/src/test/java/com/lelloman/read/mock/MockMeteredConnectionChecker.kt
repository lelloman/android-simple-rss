package com.lelloman.read.mock

import com.lelloman.read.core.MeteredConnectionChecker

class MockMeteredConnectionChecker(var isNetworkMeteredValue: Boolean = false) : MeteredConnectionChecker {

    override fun isNetworkMetered() = isNetworkMeteredValue
}