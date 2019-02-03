package com.lelloman.simplerss.widget

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import com.lelloman.identicon.ClassicIdenticonView
import com.lelloman.simplerss.R

class SourceImageView(
    context: Context,
    attrs: AttributeSet
) : FrameLayout(context, attrs) {

    private val identiconView by lazy { findViewById<ClassicIdenticonView>(R.id.identicon_view) }
    private val imageView by lazy { findViewById<ImageView>(R.id.image_view) }

    init {
        inflate(context, R.layout.view_source_image_view, this)
    }

    fun setHash(hash: Int?) {
        if (hash == null) {
            identiconView.visibility = GONE
            imageView.visibility = VISIBLE
        } else {
            identiconView.setHash(hash)
            identiconView.visibility = VISIBLE
            imageView.visibility = GONE
        }
    }

    fun setImage(bitmap: Bitmap?) {
        identiconView.visibility = GONE
        imageView.visibility = VISIBLE
        imageView.setImageBitmap(bitmap)
    }
}