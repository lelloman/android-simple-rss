package com.lelloman.read.testutils.screen

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.BoundedMatcher
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.view.View
import com.lelloman.common.view.AppTheme
import com.lelloman.common.widget.PagerIndicator
import com.lelloman.read.R
import com.lelloman.testutils.Screen
import com.lelloman.testutils.checkRecyclerViewCount
import com.lelloman.testutils.clickView
import com.lelloman.testutils.clickViewWithText
import com.lelloman.testutils.viewIsDisplayed
import com.lelloman.testutils.viewWithTextIsDisplayed
import org.hamcrest.Description


class WalkthroughScreen : Screen() {

    init {
        viewIsDisplayed(R.id.walkthrough_root)
    }

    fun firstPageIsDisplayed() = apply { viewWithTextIsDisplayed(string(R.string.walkthrough_first_page)) }

    fun clickOk() = apply { clickView(R.id.button_ok) }

    fun themesAreDisplayed() = apply {
        viewWithTextIsDisplayed(AppTheme.LIGHT.name)
        viewWithTextIsDisplayed(AppTheme.DARCULA.name)
        checkRecyclerViewCount(AppTheme.values().size, R.id.themes_recycler_view)
    }

    fun clickOnThemes() = apply {
        clickViewWithText(AppTheme.DARCULA.name)
        clickViewWithText(AppTheme.LIGHT.name)
    }

    fun swipeLeft() = apply { com.lelloman.testutils.swipeLeft(R.id.view_pager) }
    fun swipeRight() = apply { com.lelloman.testutils.swipeRight(R.id.view_pager) }

    fun typeInUrlIsDisplayed() = apply { viewWithTextIsDisplayed(string(R.string.type_in_url)) }

    fun clickNo() = with(clickViewWithText(string(R.string.NO))) {
        ArticlesListScreen()
    }

    fun pressClose() = with(clickView(R.id.skip)) { ArticlesListScreen() }

    fun assertPagerIndicatorColor(color: Int) = apply {
        onView(withId(R.id.pager_indicator))
            .check(matches(object : BoundedMatcher<View, PagerIndicator>(PagerIndicator::class.java) {
                public override fun matchesSafely(view: PagerIndicator): Boolean {
                    return color == view.fillColor
                }

                override fun describeTo(description: Description) {
                    description.appendText("with pager indicator color: ")
                }
            }))
    }

    fun clickOnTheme(theme: AppTheme) = apply { clickViewWithText(theme.name) }
}