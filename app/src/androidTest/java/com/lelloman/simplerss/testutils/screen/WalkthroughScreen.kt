package com.lelloman.simplerss.testutils.screen

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.lelloman.common.view.AppTheme
import com.lelloman.common.widget.PagerIndicator
import com.lelloman.instrumentedtestutils.Screen
import com.lelloman.instrumentedtestutils.ViewActions.clickView
import com.lelloman.instrumentedtestutils.ViewActions.clickViewWithText
import com.lelloman.instrumentedtestutils.ViewAssertions.checkRecyclerViewCount
import com.lelloman.instrumentedtestutils.ViewAssertions.checkViewIsDisplayed
import com.lelloman.instrumentedtestutils.ViewAssertions.checkViewWithTextIsDisplayed
import com.lelloman.simplerss.R
import org.hamcrest.Description


class WalkthroughScreen : Screen() {

    init {
        checkViewIsDisplayed(R.id.walkthrough_root)
    }

    fun firstPageIsDisplayed() = apply { checkViewIsDisplayed(R.id.text_view_first_page) }

    fun secondPageIsDisplayed() = apply { checkViewWithTextIsDisplayed(string(R.string.walkthrough_second_page)) }

    fun clickNext() = apply { clickView(R.id.next) }

    fun themesAreDisplayed() = apply {
        checkViewWithTextIsDisplayed(AppTheme.LIGHT.name)
        checkViewWithTextIsDisplayed(AppTheme.DARCULA.name)
        checkRecyclerViewCount(AppTheme.values().size, R.id.themes_recycler_view)
    }

    fun clickOnThemes() = apply {
        clickViewWithText(AppTheme.DARCULA.name)
        clickViewWithText(AppTheme.LIGHT.name)
    }

    fun swipeLeft() = apply { com.lelloman.instrumentedtestutils.ViewActions.swipeLeft(R.id.view_pager) }
    fun swipeRight() = apply { com.lelloman.instrumentedtestutils.ViewActions.swipeRight(R.id.view_pager) }

    fun typeInUrlIsDisplayed() = apply { checkViewWithTextIsDisplayed(string(R.string.type_in_url)) }

    fun clickNo() = clickViewWithText(string(R.string.NO)).also {
        ArticlesListScreen()
    }

    fun pressClose() = clickView(R.id.skip).run { ArticlesListScreen() }

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