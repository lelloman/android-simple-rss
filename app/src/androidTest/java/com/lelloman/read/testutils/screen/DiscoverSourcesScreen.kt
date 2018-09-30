package com.lelloman.read.testutils.screen

import android.support.test.espresso.Espresso.closeSoftKeyboard
import android.support.test.espresso.Espresso.pressBack
import com.lelloman.read.R
import com.lelloman.testutils.Screen
import com.lelloman.testutils.clickView
import com.lelloman.testutils.typeInEditText
import com.lelloman.testutils.viewWithTextIsDisplayed

class DiscoverSourcesScreen : Screen() {

    init {
        viewWithTextIsDisplayed(string(R.string.type_in_url))
    }

    fun typeUrl(url: String) = apply { typeInEditText(R.id.edit_text_url, url) }

    fun clickOnDiscover() = clickView(R.id.button_discover).run {
        FoundFeedsScreen()
    }

    fun hasText(text: String) = apply { viewWithTextIsDisplayed(text) }

    fun closeKeyboard() = apply { closeSoftKeyboard() }

    fun backToArticlesList() = pressBack().run { ArticlesListScreen() }
}