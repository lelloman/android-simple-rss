package com.lelloman.read.testutils.screen

import android.support.test.InstrumentationRegistry
import com.lelloman.testutils.viewIsDisplayed
import com.lelloman.testutils.viewWithTextIsDisplayed

abstract class Screen {

    private val context = InstrumentationRegistry.getTargetContext()

    protected fun viewVisible(id: Int) = viewIsDisplayed(id)
    protected fun viewVisible(text: String) = viewWithTextIsDisplayed(text)

    protected fun string(id: Int) = context.getString(id)

}