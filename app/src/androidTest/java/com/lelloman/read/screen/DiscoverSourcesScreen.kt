package com.lelloman.read.screen

import android.support.test.espresso.Espresso.closeSoftKeyboard
import android.support.test.espresso.Espresso.pressBack
import com.lelloman.read.R
import com.lelloman.read.testutils.clickView
import com.lelloman.read.testutils.typeInEditText
import com.lelloman.read.testutils.viewWithTextIsDisplayed

class DiscoverSourcesScreen : Screen() {

    init {
        viewWithTextIsDisplayed(string(R.string.type_in_url))
    }

    fun typeUrl(url: String) = apply { typeInEditText(R.id.edit_text_url, url) }

    fun clickOnDiscover() = with(clickView(R.id.button_discover)) {
        FoundFeedsScreen()
    }

    fun hasText(text: String) = apply { viewWithTextIsDisplayed(text) }

    fun closeKeyboard() = apply { closeSoftKeyboard() }

    fun backToArticlesList() = with(pressBack()) { ArticlesListScreen() }
}