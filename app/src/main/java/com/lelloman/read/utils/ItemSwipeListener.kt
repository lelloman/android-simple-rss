package com.lelloman.read.utils

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper

object ItemSwipeListener {

    fun set(recyclerView: RecyclerView, onSwipeListener: (position: Int) -> Unit) {

        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                onSwipeListener.invoke(viewHolder.adapterPosition)
            }
        }

        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(recyclerView)
    }
}