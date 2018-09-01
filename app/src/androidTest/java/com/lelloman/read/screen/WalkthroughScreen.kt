package com.lelloman.read.screen

import com.lelloman.read.R
import com.lelloman.read.core.view.AppTheme
import com.lelloman.read.testutils.clickView
import com.lelloman.read.testutils.clickViewWithText
import com.lelloman.read.testutils.viewIsDisplayed
import com.lelloman.read.testutils.viewWithTextIsDisplayed


class WalkthroughScreen : Screen() {

    init {
        viewIsDisplayed(R.id.walkthrough_root)
    }

    fun firstPageIsDisplayed() = apply { viewWithTextIsDisplayed(string(R.string.walkthrough_first_page)) }

    fun clickOk() = apply { clickView(R.id.button_ok) }

    fun themesAreDisplayed() = apply {
        viewWithTextIsDisplayed(AppTheme.LIGHT.name)
        viewWithTextIsDisplayed(AppTheme.DARCULA.name)
    }

    fun clickOnThemes() = apply {
        clickViewWithText(AppTheme.DARCULA.name)
        clickViewWithText(AppTheme.LIGHT.name)
    }

    fun swipeLeft() = apply { com.lelloman.read.testutils.swipeLeft(R.id.view_pager) }
    fun swipeRight() = apply { com.lelloman.read.testutils.swipeRight(R.id.view_pager) }

    fun typeInUrlIsDisplayed() = apply { viewWithTextIsDisplayed(string(R.string.type_in_url)) }

    fun clickNo() = with(clickViewWithText(string(R.string.NO))) {
        ArticlesListScreen()
    }
}