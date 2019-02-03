package com.lelloman.simplerss.html

import android.os.Build
import android.text.Html
import android.text.Spanned

@Deprecated(message = "Move this in common")
class HtmlSpanner {

    fun fromHtml(text: String): Spanned = if (Build.VERSION.SDK_INT >= 24) {
        Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
    } else {
        Html.fromHtml(text)
    }
}