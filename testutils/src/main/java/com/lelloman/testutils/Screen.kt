package com.lelloman.testutils

import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.support.test.InstrumentationRegistry


abstract class Screen {

    private val context = InstrumentationRegistry.getTargetContext()

    protected fun viewVisible(@IdRes id: Int) = viewIsDisplayed(id)
    protected fun viewVisible(text: String) = viewWithTextIsDisplayed(text)

    protected fun string(@StringRes id: Int): String = context.getString(id)
    protected fun string(@StringRes id: Int, vararg arg: Any): String = context.getString(id, *arg)
}