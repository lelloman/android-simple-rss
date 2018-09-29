package com.lelloman.read.testutils.screen

import android.graphics.drawable.VectorDrawable
import android.support.test.espresso.Espresso.pressBack
import android.widget.EditText
import com.google.common.truth.Truth.assertThat
import com.lelloman.read.R
import com.lelloman.testutils.Screen
import com.lelloman.testutils.clickView
import com.lelloman.testutils.clickViewWithText
import com.lelloman.testutils.typeInEditText
import com.lelloman.testutils.viewIsDisplayed
import com.lelloman.testutils.viewWithId

class AddSourceScreen : Screen() {
    init {
        viewIsDisplayed(R.id.add_source_root)
    }

    fun backToSourcesList() = with(pressBack()) { SourcesListScreen() }

    fun typeSourceName(name: String) = apply { typeInEditText(R.id.edit_text_source_name, name) }

    fun typeSourceUrl(url: String) = apply { typeInEditText(R.id.edit_text_source_url, url) }

    fun clickTestUrl() = apply { clickView(R.id.button_test_source) }

    fun urlFieldHasNoDrawable() = apply {
        viewWithId(R.id.edit_text_source_url).check { view, _ ->
            assertThat(view).isInstanceOf(EditText::class.java)
            (view as EditText).apply {
                view.compoundDrawables.forEach {
                    assertThat(it).isNull()
                }
            }
        }
    }

    fun wait(seconds: Double) = apply { com.lelloman.testutils.wait(seconds) }

    fun urlFieldShowsOkDrawable() = apply {
        viewWithId(R.id.edit_text_source_url).check { view, noViewFoundException ->
            assertThat(view).isInstanceOf(EditText::class.java)
            (view as EditText).apply {
                view.compoundDrawables.forEachIndexed { index, drawable ->
                    if (index != 2) {
                        assertThat(drawable).isNull()
                    } else {
                        assertThat(drawable).isInstanceOf(VectorDrawable::class.java)
                    }
                }
            }
        }
    }

    fun clickSave() = with(clickViewWithText(string(R.string.SAVE))) { SourcesListScreen() }
}