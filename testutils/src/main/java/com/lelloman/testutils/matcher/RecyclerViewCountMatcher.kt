package com.lelloman.testutils.matcher

import android.support.test.espresso.matcher.BoundedMatcher
import android.support.v7.widget.RecyclerView
import android.view.View
import org.hamcrest.Description


class RecyclerViewCountMatcher(private val expectedCount: Int)
    : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {

    override fun describeTo(description: Description) {
        description.appendText("check RecyclerView count $expectedCount")
    }

    override fun matchesSafely(item: RecyclerView) = item.adapter.itemCount == expectedCount
}