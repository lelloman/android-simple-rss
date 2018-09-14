package com.lelloman.read.utils

import android.databinding.BindingAdapter
import android.graphics.Bitmap
import android.support.design.widget.TextInputLayout
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.lelloman.common.utils.ByteArrayWithId
import com.lelloman.identicon.ClassicIdenticonView
import com.lelloman.read.R
import com.lelloman.read.ReadApplication
import com.lelloman.read.widget.SourceImageView

interface OnKeyboardActionDoneListener {
    fun onKeyboardActionDone()
}

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("app:hash")
    fun bindIdenticonHash(identiconView: ClassicIdenticonView, hash: Int) {
        identiconView.setHash(hash)
    }

    @JvmStatic
    @BindingAdapter("app:viewVisible")
    fun bindViewVisibility(view: View, isVisible: Boolean) {
        view.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("app:isRefreshing")
    fun bindIsSwipeRefreshing(view: SwipeRefreshLayout, isRefreshing: Boolean) {
        view.isRefreshing = isRefreshing
    }

    @JvmStatic
    @BindingAdapter("app:error")
    fun bindError(view: TextInputLayout, text: String?) {
        view.error = text
        view.isErrorEnabled = !text.isNullOrBlank()
    }

    @JvmStatic
    @BindingAdapter("app:imageUrl")
    fun bindImageurl(view: ImageView, url: String?) {
        url?.let {
            ReadApplication.getPicassoWrap()
                .loadUrlIntoImageView(
                    uri = url,
                    view = view,
                    placeHolderId = R.drawable.ic_image_24dp
                )
        }
    }

    @JvmStatic
    @BindingAdapter("app:imageBytes")
    fun bindImageViewImageBytes(view: ImageView, byteArrayWithId: ByteArrayWithId) {
        byteArrayWithId.bind(view::setImageBitmap)
    }

    @JvmStatic
    @BindingAdapter("app:imageBytes")
    fun bindSourceImageViewImageBytes(view: SourceImageView, byteArrayWithId: ByteArrayWithId) {
        byteArrayWithId.bind(view::setImage)
    }

    private fun ByteArrayWithId.bind(action: (Bitmap) -> Unit) {
        byteArray?.let {
            ReadApplication
                .getFaviconBitmapProvider()
                .getFaviconBitmap(byteArray!!, id)
                ?.let(action)
        }
    }

    @JvmStatic
    @BindingAdapter("app:editTextDrawable")
    fun bindEditTextDrawable(editText: EditText, resId: Int) {
        editText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, resId, 0)
    }

    @JvmStatic
    @BindingAdapter("app:onKeyboardActionDoneListener")
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