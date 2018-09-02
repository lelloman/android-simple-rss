package com.lelloman.read.testutils.screen

import android.graphics.drawable.VectorDrawable
import android.support.test.espresso.Espresso.pressBack
import android.widget.EditText
import com.lelloman.read.R
import com.lelloman.read.testutils.clickView
import com.lelloman.read.testutils.clickViewWithText
import com.lelloman.read.testutils.typeInEditText
import com.lelloman.read.testutils.viewIsDisplayed
import com.lelloman.read.testutils.viewWithId
import junit.framework.Assert

class AddSourceScreen : Screen() {
    init {
        viewIsDisplayed(R.id.add_source_root)
    }

    fun backToSourcesList() = with(pressBack()) { SourcesListScreen() }

    fun typeSourceName(name: String) = apply { typeInEditText(R.id.edit_text_source_name, name) }

    fun typeSourceUrl(url: String) = apply { typeInEditText(R.id.edit_text_source_url, url) }

    fun clickTestUrl() = apply { clickView(R.id.button_test_source) }

    fun urlFieldHasNoDrawable() = apply {
        viewWithId(R.id.edit_text_source_url).check { view, noViewFoundException ->
            Assert.assertTrue(view is EditText)
            (view as EditText).apply {
                view.compoundDrawables.forEach {
                    Assert.assertEquals(null, it)
                }
            }
        }
    }

    fun wait(seconds: Double) = apply { com.lelloman.read.testutils.wait(seconds) }

    fun urlFieldShowsOkDrawable() = apply {
        viewWithId(R.id.edit_text_source_url).check { view, noViewFoundException ->
            Assert.assertTrue(view is EditText)
            (view as EditText).apply {
                view.compoundDrawables.forEachIndexed { index, drawable ->
                    if (index != 2) {
                        Assert.assertEquals(null, drawable)
                    } else {
                        Assert.assertTrue(drawable is VectorDrawable)
                    }
                }
            }
        }
    }

    fun clickSave() = with(clickViewWithText(string(R.string.SAVE))) { SourcesListScreen() }
}