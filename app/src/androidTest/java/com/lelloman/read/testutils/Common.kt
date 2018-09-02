package com.lelloman.read.testutils

import android.support.test.InstrumentationRegistry
import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.action.GeneralLocation
import android.support.test.espresso.action.GeneralSwipeAction
import android.support.test.espresso.action.Press
import android.support.test.espresso.action.Swipe
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.uiautomator.UiDevice
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.lelloman.read.R
import com.lelloman.read.testutils.matcher.AtPositionMatcher
import com.lelloman.read.testutils.matcher.RecyclerViewCountMatcher
import com.lelloman.read.testutils.matcher.SwipeRefreshLayoutMatcher
import com.lelloman.read.widget.ToggleSettingItemView
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.mockito.Mockito.`when`
import org.mockito.stubbing.OngoingStubbing


fun <T> whenever(methodCall: T): OngoingStubbing<T> = `when`(methodCall)

fun viewIsDisplayed(id: Int) {
    onView(withId(id)).checkMatches(isDisplayed())
}

fun viewWithTextIsDisplayed(text: String) {
    viewWithText(text).checkMatches(isDisplayed())
}

fun wait(seconds: Double) = Thread.sleep((seconds * 1000).toLong())

fun viewWithId(id: Int): ViewInteraction = onView(withId(id))

fun viewWithText(text: String): ViewInteraction = onView(withText(text))

fun clickView(id: Int) {
    viewWithId(id).perform(click())
}

fun clickViewWithText(text: String) {
    viewWithText(text).perform(click())
}

fun swipeRight(id: Int) {
    viewWithId(id).perform(GeneralSwipeAction(
        Swipe.FAST,
        GeneralLocation.CENTER_RIGHT,
        GeneralLocation.CENTER_LEFT,
        Press.FINGER
    ))
}

fun swipeLeft(id: Int) {
    viewWithId(id).perform(GeneralSwipeAction(
        Swipe.FAST,
        GeneralLocation.CENTER_LEFT,
        GeneralLocation.CENTER_RIGHT,
        Press.FINGER
    ))
}

fun typeInEditText(id: Int, text: String) {
    viewWithId(id).perform(typeText(text))
}

fun openOverflowMenu() {
    Espresso.openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
}

fun ViewInteraction.checkMatches(matcher: Matcher<View>)
    : ViewInteraction = check(matches(matcher))

fun onUiThread(action: () -> Unit) = InstrumentationRegistry
    .getInstrumentation()
    .runOnMainSync(action)

fun checkIsSwipeRefreshing(isRefreshing: Boolean, id: Int = R.id.swipe_refresh_layout)
    : ViewInteraction = viewWithId(id).checkMatches(SwipeRefreshLayoutMatcher(isRefreshing))

fun checkRecyclerViewCount(count: Int, id: Int = R.id.recycler_view)
    : ViewInteraction = viewWithId(id).checkMatches(RecyclerViewCountMatcher(count))

fun rotateNatural() = UiDevice.getInstance(getInstrumentation()).setOrientationNatural()

fun rotateLeft() = UiDevice.getInstance(getInstrumentation()).setOrientationLeft()

fun rotateRight() = UiDevice.getInstance(getInstrumentation()).setOrientationRight()

fun checkViewAtPositionHasText(position: Int, text: String, id: Int = R.id.recycler_view) {
    viewWithId(id)
        .check(matches(AtPositionMatcher(position, hasDescendant(withText(text)))))
}

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

fun checkViewAtPositionImageViewVisibility(
    position: Int,
    recyclerViewId: Int,
    imageViewId: Int,
    visibility: Int
) {
    viewWithId(recyclerViewId)
        .check(matches(AtPositionMatcher(position, object : BaseMatcher<View>() {
            override fun describeTo(description: Description?) = Unit

            override fun matches(item: Any?): Boolean {
                val view = item as View

                val imageView = view.findViewById<ImageView>(imageViewId)
                return imageView.visibility == visibility
            }
        })))
}

fun setToggleSettingChecked(isChecked: Boolean) = object : ViewAction {
    override fun getDescription(): String = "setChecked($isChecked)"

    override fun getConstraints() = object : BaseMatcher<View>() {
        override fun describeTo(description: Description?) = Unit

        override fun matches(item: Any?) = item is ToggleSettingItemView
    }

    override fun perform(uiController: UiController?, view: View?) {
        (view as ToggleSettingItemView).setChecked(isChecked)
    }
}

fun clickOnRecyclerViewItem(position: Int, recyclerViewId: Int): ViewInteraction =
    viewWithId(recyclerViewId)
        .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(position, click()))