package com.lelloman.common.utils

import android.databinding.BindingAdapter
import android.view.View

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("viewVisible")
    fun bindViewVisibility(view: View, isVisible: Boolean) {
        view.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}