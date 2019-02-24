package com.lelloman.simplerss.testutils.screen

import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.lelloman.common.view.AppTheme
import com.lelloman.instrumentedtestutils.Screen
import com.lelloman.instrumentedtestutils.ViewActions.clickView
import com.lelloman.instrumentedtestutils.ViewAssertions.checkViewIsDisplayed
import com.lelloman.instrumentedtestutils.viewWithId
import com.lelloman.simplerss.R
import com.lelloman.simplerss.testutils.setToggleSettingChecked
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Description

class SettingsScreen : Screen() {
    init {
        checkViewIsDisplayed(R.id.settings_root)
    }

    fun backToArticlesList() = pressBack().run { ArticlesListScreen() }

    fun setOpenArticlesInApp(openInApp: Boolean) = apply {
        viewWithId(R.id.toggle_setting_open_articles_in_app).perform(setToggleSettingChecked(openInApp))
    }

    fun setArticlesImages(hasImages: Boolean) = apply {
        viewWithId(R.id.toggle_setting_artcles_images).perform(setToggleSettingChecked(hasImages))
    }

    fun setUseMeteredNetwork(useMeteredNetwork: Boolean) = apply {
        viewWithId(R.id.toggle_setting_use_metered_network).perform(setToggleSettingChecked(useMeteredNetwork))
    }

    fun assertPrimaryColor(color: Int) = apply {
        onView(withId(R.id.toolbar))
            .check(matches(object : BoundedMatcher<View, Toolbar>(Toolbar::class.java) {
                public override fun matchesSafely(view: Toolbar): Boolean {
                    return color == (view.background as ColorDrawable).color
                }

                override fun describeTo(description: Description) {
                    description.appendText("with toolbar bg color: ")
                }
            }))
    }

    fun setTheme(theme: AppTheme) = apply {
        onView(withId(R.id.spinner_themes)).perform(ViewActions.click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`(theme.name))).perform(click())
    }

    fun clickOnClearData() = apply {
        viewWithId(R.id.clear_data).perform(scrollTo())
        clickView(R.id.clear_data)
    }
}