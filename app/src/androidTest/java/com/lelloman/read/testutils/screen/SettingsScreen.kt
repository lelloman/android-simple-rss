package com.lelloman.read.testutils.screen

import android.graphics.drawable.ColorDrawable
import android.support.test.espresso.Espresso.*
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.BoundedMatcher
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.v7.widget.Toolbar
import android.view.View
import com.lelloman.common.view.AppTheme
import com.lelloman.read.R
import com.lelloman.read.testutils.setToggleSettingChecked
import com.lelloman.testutils.Screen
import com.lelloman.testutils.viewIsDisplayed
import com.lelloman.testutils.viewWithId
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Description

class SettingsScreen : Screen() {
    init {
        viewIsDisplayed(R.id.settings_root)
    }

    fun backToArticlesList() = with(pressBack()) { ArticlesListScreen() }

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
}