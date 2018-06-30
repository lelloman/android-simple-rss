package com.lelloman.read.testutils

import android.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule

abstract class AndroidArchTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    abstract fun setUp()
}