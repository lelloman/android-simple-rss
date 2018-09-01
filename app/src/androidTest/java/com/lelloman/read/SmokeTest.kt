package com.lelloman.read

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.closeSoftKeyboard
import android.support.test.espresso.Espresso.pressBack
import android.support.test.rule.ActivityTestRule
import com.lelloman.read.core.view.AppTheme
import com.lelloman.read.testutils.TestApp
import com.lelloman.read.testutils.clickView
import com.lelloman.read.testutils.clickViewWithText
import com.lelloman.read.testutils.openOverflowMenu
import com.lelloman.read.testutils.rotateNatural
import com.lelloman.read.testutils.swipeLeft
import com.lelloman.read.testutils.swipeRight
import com.lelloman.read.testutils.typeInEditText
import com.lelloman.read.testutils.viewIsDisplayed
import com.lelloman.read.testutils.viewWithText
import com.lelloman.read.testutils.viewWithTextIsDisplayed
import com.lelloman.read.testutils.wait
import com.lelloman.read.ui.launcher.view.LauncherActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SmokeTest {

    @get:Rule
    val activityTestRule = ActivityTestRule<LauncherActivity>(LauncherActivity::class.java, true, false)

    private val context = InstrumentationRegistry.getTargetContext()

    private fun string(id: Int) = context.getString(id)

    @Before
    fun setUp() {
        rotateNatural()
        TestApp.instance.appSettings.reset()
        TestApp.instance.db.clearAllTables()

        activityTestRule.launchActivity(null)
    }

    private fun fromWalkthroughToArticlesList() {
        viewIsDisplayed(R.id.walkthrough_root)
        viewWithTextIsDisplayed(string(R.string.walkthrough_first_page))
        clickView(R.id.button_ok)
        viewWithTextIsDisplayed(AppTheme.LIGHT.name)
        viewWithTextIsDisplayed(AppTheme.DARCULA.name)
        clickViewWithText(AppTheme.DARCULA.name)
        clickViewWithText(AppTheme.LIGHT.name)
        swipeLeft(R.id.view_pager)
        viewWithTextIsDisplayed(string(R.string.walkthrough_first_page))
        swipeRight(R.id.view_pager)
        swipeRight(R.id.view_pager)
        viewWithText(string(R.string.type_in_url))
        swipeRight(R.id.view_pager)
        clickViewWithText(string(R.string.NO))
    }

    private fun articlesListNavigation() {
        // sources
        openOverflowMenu()
        clickViewWithText(string(R.string.sources))
        viewIsDisplayed(R.id.sources_recycler_view)
        clickView(R.id.fab)
        viewIsDisplayed(R.id.add_source_root)
        pressBack()
        viewIsDisplayed(R.id.sources_recycler_view)
        pressBack()

        // settings
        openOverflowMenu()
        clickViewWithText(string(R.string.settings))
        viewIsDisplayed(R.id.settings_root)
        pressBack()

        // discover
        openOverflowMenu()
        clickViewWithText(string(R.string.discover_sources))
        viewWithTextIsDisplayed(string(R.string.type_in_url))
        typeInEditText(R.id.edit_text_url, "www.asd.com")
        closeSoftKeyboard()
        clickView(R.id.button_discover)
        viewWithTextIsDisplayed("http://www.asd.com")
        pressBack()
        viewWithTextIsDisplayed(string(R.string.type_in_url))
        viewWithTextIsDisplayed("http://www.asd.com")
        pressBack()
    }

    @Test
    fun smokeTest() {
        fromWalkthroughToArticlesList()
        articlesListNavigation()

        wait(5.0)
    }
}