package com.lelloman.testutils.matcher

import android.support.test.espresso.matcher.BoundedMatcher
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import org.hamcrest.Description

class SwipeRefreshLayoutMatcher(private val isRefreshing: Boolean)
    : BoundedMatcher<View, SwipeRefreshLayout>(SwipeRefreshLayout::class.java) {

    override fun describeTo(description: Description) {
        description.appendText("check isRefreshing $isRefreshing")
    }

    override fun matchesSafely(item: SwipeRefreshLayout) = item.isRefreshing == isRefreshing
}