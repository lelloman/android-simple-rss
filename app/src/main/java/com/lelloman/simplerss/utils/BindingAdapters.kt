package com.lelloman.simplerss.utils

import android.databinding.BindingAdapter
import android.graphics.Bitmap
import android.support.design.widget.TextInputLayout
import android.support.v4.widget.SwipeRefreshLayout
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.lelloman.common.utils.model.ByteArrayWithId
import com.lelloman.identicon.ClassicIdenticonView
import com.lelloman.simplerss.R
import com.lelloman.simplerss.SimpleRssApplication
import com.lelloman.simplerss.SimpleRssApplication.Companion.getPicassoWrap
import com.lelloman.simplerss.widget.SourceImageView

interface OnKeyboardActionDoneListener {
    fun onKeyboardActionDone()
}

object BindingAdapters {

    @Suppress("unused")
    @JvmStatic
    @BindingAdapter("hash")
    fun bindIdenticonHash(identiconView: ClassicIdenticonView, hash: Int) {
        identiconView.setHash(hash)
    }

    @JvmStatic
    @BindingAdapter("isRefreshing")
    fun bindIsSwipeRefreshing(view: SwipeRefreshLayout, isRefreshing: Boolean) {
        view.isRefreshing = isRefreshing
    }

    @JvmStatic
    @BindingAdapter("error")
    fun bindError(view: TextInputLayout, text: String?) {
        view.error = text
        view.isErrorEnabled = !text.isNullOrBlank()
    }

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun bindImageUrl(view: ImageView, url: String?) {
        url?.let {
            getPicassoWrap()
                .loadUrlIntoImageView(
                    uri = url,
                    view = view,
                    placeHolderId = R.drawable.ic_image_24dp
                )
        }
    }

    @JvmStatic
    @BindingAdapter("imageBytes")
    fun bindImageViewImageBytes(view: ImageView, byteArrayWithId: ByteArrayWithId) {
        byteArrayWithId.bind(view::setImageBitmap)
    }

    @JvmStatic
    @BindingAdapter("imageBytes")
    fun bindSourceImageViewImageBytes(view: SourceImageView, byteArrayWithId: ByteArrayWithId) {
        byteArrayWithId.bind(view::setImage)
    }

    private fun ByteArrayWithId.bind(action: (Bitmap) -> Unit) {
        byteArray?.let {
            SimpleRssApplication
                .getFaviconBitmapProvider()
                .getFaviconBitmap(byteArray!!, id)
                ?.let(action)
        }
    }

    @JvmStatic
    @BindingAdapter("editTextDrawable")
    fun bindEditTextDrawable(editText: EditText, resId: Int) {
        editText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, resId, 0)
    }

    @JvmStatic
    @BindingAdapter("onKeyboardActionDoneListener")
    fun setOnKeyboardActionDoneListener(view: TextView, listener: OnKeyboardActionDoneListener?) {
        if (listener == null) {
            view.setOnEditorActionListener(null)
        } else {
            view.setOnEditorActionListener { _, actionId, _ ->
                (actionId == EditorInfo.IME_ACTION_DONE)
                    .also { if (it) listener.onKeyboardActionDone() }
            }
        }

    }
}