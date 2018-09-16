package com.lelloman.testutils

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.action.GeneralLocation
import android.support.test.espresso.action.GeneralSwipeAction
import android.support.test.espresso.action.Press
import android.support.test.espresso.action.Swipe
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.lelloman.testutils.matcher.AtPositionMatcher
import com.lelloman.testutils.matcher.RecyclerViewCountMatcher
import com.lelloman.testutils.matcher.SwipeRefreshLayoutMatcher
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher


fun viewWithId(id: Int): ViewInteraction = Espresso.onView(ViewMatchers.withId(id))

fun viewWithText(text: String): ViewInteraction = Espresso.onView(ViewMatchers.withText(text))

fun viewIsDisplayed(id: Int) {
    Espresso.onView(ViewMatchers.withId(id)).checkMatches(ViewMatchers.isDisplayed())
}

fun viewWithTextIsDisplayed(text: String) {
    viewWithText(text).checkMatches(ViewMatchers.isDisplayed())
}

fun wait(seconds: Double) = Thread.sleep((seconds * 1000).toLong())

fun ViewInteraction.checkMatches(matcher: Matcher<View>)
    : ViewInteraction = check(ViewAssertions.matches(matcher))

fun checkIsSwipeRefreshing(isRefreshing: Boolean, id: Int)
    : ViewInteraction = viewWithId(id).checkMatches(SwipeRefreshLayoutMatcher(isRefreshing))

fun checkRecyclerViewCount(count: Int, id: Int)
    : ViewInteraction = viewWithId(id).checkMatches(RecyclerViewCountMatcher(count))

fun checkViewAtPositionHasText(position: Int, text: String, id: Int) {
    viewWithId(id)
        .check(ViewAssertions.matches(AtPositionMatcher(position, ViewMatchers.hasDescendant(ViewMatchers.withText(text)))))
}

fun swipeLeft(id: Int) {
    viewWithId(id).perform(GeneralSwipeAction(
        Swipe.FAST,
        GeneralLocation.CENTER_LEFT,
        GeneralLocation.CENTER_RIGHT,
        Press.FINGER
    ))
}

fun swipeRight(id: Int) {
    viewWithId(id).perform(GeneralSwipeAction(
        Swipe.FAST,
        GeneralLocation.CENTER_RIGHT,
        GeneralLocation.CENTER_LEFT,
        Press.FINGER
    ))
}

fun typeInEditText(id: Int, text: String) {
    viewWithId(id).perform(ViewActions.typeText(text))
}

fun openOverflowMenu() {
    Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
}

fun clickView(id: Int) {
    viewWithId(id).perform(ViewActions.click())
}

fun clickViewWithText(text: String) {
    viewWithText(text).perform(ViewActions.click())
}

fun onUiThread(action: () -> Unit) = InstrumentationRegistry
    .getInstrumentation()
    .runOnMainSync(action)

fun checkViewAtPositionHasImageVisible(position: Int, recyclerViewId: Int, imageViewId: Int) =
    checkViewAtPositionImageViewVisibility(
        position = position,
        recyclerViewId = recyclerViewId,
        imageViewId = imageViewId,
        visibility = View.VISIBLE
    )

fun checkViewAtPositionHasImageGone(position: Int, recyclerViewId: Int, imageViewId: Int) =
    checkViewAtPositionImageViewVisibility(
        position = position,
        recyclerViewId = recyclerViewId,
        imageViewId = imageViewId,
        visibility = View.GONE
    )

fun clickOnRecyclerViewItem(position: Int, recyclerViewId: Int): ViewInteraction =
    viewWithId(recyclerViewId)
        .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(position, ViewActions.click()))

fun checkViewAtPositionImageViewVisibility(
    position: Int,
    recyclerViewId: Int,
    imageViewId: Int,
    visibility: Int
) {
    viewWithId(recyclerViewId)
        .check(ViewAssertions.matches(AtPositionMatcher(position, object : BaseMatcher<View>() {
            override fun describeTo(description: Description?) = Unit

            override fun matches(item: Any?): Boolean {
                val view = item as View

                val imageView = view.findViewById<ImageView>(imageViewId)
                return imageView.visibility == visibility
            }
        })))
}
