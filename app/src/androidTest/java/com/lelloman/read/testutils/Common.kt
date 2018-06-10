package com.lelloman.read.testutils

import org.mockito.Mockito.`when`
import org.mockito.stubbing.OngoingStubbing

fun <T> whenever(methodCall: T): OngoingStubbing<T> = `when`(methodCall)