package com.lelloman.testutils.matcher

import android.support.test.espresso.matcher.BoundedMatcher
import android.support.v7.widget.RecyclerView
import android.view.View
import org.hamcrest.Description
import org.hamcrest.Matcher

class AtPositionMatcher(private val position: Int, private val itemMatcher: Matcher<View>)
    : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {

    override fun describeTo(description: Description) {
        description.appendText("has item at position $position: ")
        itemMatcher.describeTo(description)
    }

    override fun matchesSafely(view: RecyclerView): Boolean {
        val viewHolder: RecyclerView.ViewHolder = view.findViewHolderForAdapterPosition(position)
            ?: // has no item on such position
            return false

        return itemMatcher.matches(viewHolder.itemView)
    }
}