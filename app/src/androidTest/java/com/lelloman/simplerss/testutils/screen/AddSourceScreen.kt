package com.lelloman.simplerss.testutils.screen

import android.graphics.drawable.VectorDrawable
import android.widget.EditText
import androidx.test.espresso.Espresso.pressBack
import com.google.common.truth.Truth.assertThat
import com.lelloman.common.androidtestutils.Screen
import com.lelloman.common.androidtestutils.ViewActions.clickView
import com.lelloman.common.androidtestutils.ViewActions.clickViewWithText
import com.lelloman.common.androidtestutils.ViewActions.typeInEditText
import com.lelloman.common.androidtestutils.ViewAssertions.checkViewIsDisplayed
import com.lelloman.common.androidtestutils.viewWithId
import com.lelloman.simplerss.R

class AddSourceScreen : Screen() {
    init {
        checkViewIsDisplayed(R.id.add_source_root)
    }

    fun backToSourcesList() = pressBack().run { SourcesListScreen() }

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

    fun urlFieldShowsOkDrawable() = apply {
        viewWithId(R.id.edit_text_source_url).check { view, noViewFoundException ->
            assertThat(view).isInstanceOf(EditText::class.java)
            assertThat(noViewFoundException).isNull()
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

    fun clickSave() = clickViewWithText(string(R.string.SAVE)).run { SourcesListScreen() }
}