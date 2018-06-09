package com.lelloman.read.utils

import android.databinding.BindingAdapter
import com.lelloman.identicon.ClassicIdenticonView

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("app:hash")
    fun bindIdenticonHash(identiconView: ClassicIdenticonView, hash: Int) {
        identiconView.setHash(hash)
    }
}