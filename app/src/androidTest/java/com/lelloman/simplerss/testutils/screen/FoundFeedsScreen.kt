package com.lelloman.simplerss.testutils.screen

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import com.lelloman.common.androidtestutils.Screen
import com.lelloman.common.androidtestutils.ViewAssertions.checkRecyclerViewCount
import com.lelloman.common.androidtestutils.viewWithId
import com.lelloman.simplerss.R
import org.hamcrest.Description
import java.lang.Thread.sleep

class FoundFeedsScreen : Screen() {

    init {
        viewVisible(R.id.found_feed_list_root)
    }

    fun backToDiscoverUrlScreen() = pressBack().run { DiscoverSourcesScreen() }

    fun displaysFoundFeeds(expectedCount: Int) = apply {
        waitableAssertion({
            var count = 0
            viewWithId(R.id.discover_recycler_view).check(matches(object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {

                override fun describeTo(description: Description) = Unit

                override fun matchesSafely(item: RecyclerView) = true.apply { count = item.adapter!!.itemCount }
            }))
            count == expectedCount
        }, {
            checkRecyclerViewCount(expectedCount, R.id.discover_recycler_view)
        })
    }

    @Deprecated(message = "Use the one from common when available")
    private fun waitableAssertion(checkCondition: () -> Boolean, performAssertion: () -> Unit, timeOutMs: Long = 10_000) {
        val startTime = System.currentTimeMillis()
        val endTime = startTime + timeOutMs

        while (System.currentTimeMillis() != endTime) {
            if (checkCondition()) {
                break
            }
            sleep(50)
        }

        performAssertion()
    }
}