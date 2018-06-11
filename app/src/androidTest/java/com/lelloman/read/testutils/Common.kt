package com.lelloman.read.testutils

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.view.View
import com.lelloman.read.R
import com.lelloman.read.testutils.matcher.RecyclerViewCountMatcher
import com.lelloman.read.testutils.matcher.SwipeRefreshLayoutMatcher
import org.hamcrest.Matcher
import org.mockito.Mockito.`when`
import org.mockito.stubbing.OngoingStubbing


fun <T> whenever(methodCall: T): OngoingStubbing<T> = `when`(methodCall)

fun viewIsDisplayed(id: Int) {
    onView(withId(id)).check(matches(isDisplayed()))
}

fun wait(seconds: Double) = Thread.sleep((seconds * 1000).toLong())

fun viewWithId(id: Int): ViewInteraction = onView(withId(id))

fun ViewInteraction.checkMatches(matcher: Matcher<View>)
    : ViewInteraction = check(matches(matcher))

fun onUiThread(action: () -> Unit) = InstrumentationRegistry
    .getInstrumentation()
    .runOnMainSync(action)

fun checkIsSwipeRefreshing(isRefreshing: Boolean, id: Int = R.id.swipe_refresh_layout)
    : ViewInteraction = viewWithId(id).checkMatches(SwipeRefreshLayoutMatcher(isRefreshing))

fun assertRecyclerViewCount(count: Int, id: Int = R.id.recycler_view)
    : ViewInteraction = viewWithId(id).checkMatches(RecyclerViewCountMatcher(count))

