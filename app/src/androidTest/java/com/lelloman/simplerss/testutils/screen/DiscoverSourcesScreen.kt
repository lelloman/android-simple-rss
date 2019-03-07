package com.lelloman.simplerss.testutils.screen

import androidx.test.espresso.Espresso.pressBack
import com.lelloman.common.androidtestutils.Screen
import com.lelloman.common.androidtestutils.ViewActions.clickView
import com.lelloman.common.androidtestutils.ViewActions.typeInEditText
import com.lelloman.common.androidtestutils.ViewAssertions.checkViewWithTextIsDisplayed
import com.lelloman.simplerss.R

class DiscoverSourcesScreen : Screen() {

    init {
        checkViewWithTextIsDisplayed(string(R.string.type_in_url))
    }

    fun typeUrl(url: String) = apply { typeInEditText(R.id.edit_text_url, url) }

    fun clickOnDiscover() = clickView(R.id.button_discover).run {
        FoundFeedsScreen()
    }

    fun backToArticlesList() = pressBack().run { ArticlesListScreen() }
}