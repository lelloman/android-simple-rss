package com.lelloman.read.testutils

import android.support.test.InstrumentationRegistry
import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.action.GeneralLocation
import android.support.test.espresso.action.GeneralSwipeAction
import android.support.test.espresso.action.Press
import android.support.test.espresso.action.Swipe
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.uiautomator.UiDevice
import android.view.View
import com.lelloman.read.R
import com.lelloman.read.testutils.matcher.AtPositionMatcher
import com.lelloman.read.testutils.matcher.RecyclerViewCountMatcher
import com.lelloman.read.testutils.matcher.SwipeRefreshLayoutMatcher
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

fun viewWithText(text: String) : ViewInteraction = onView(withText(text))

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

fun typeInEditText(id: Int, text: String){
    viewWithId(id).perform(typeText(text))
}

fun openOverflowMenu(){
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