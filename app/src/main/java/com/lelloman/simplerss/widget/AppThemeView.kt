package com.lelloman.simplerss.widget

import android.content.Context
import android.support.v7.view.ContextThemeWrapper
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import com.lelloman.common.utils.layoutInflater
import com.lelloman.common.view.AppTheme
import com.lelloman.simplerss.R
import java.lang.Math.round


class AppThemeView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    var appTheme: AppTheme? = null
        get() = null
        set(value) {
            if (value == field || value == null) return
            removeAllViews()
            val wrappedContext = ContextThemeWrapper(context, value.resId)
            val array = wrappedContext.theme.obtainStyledAttributes(intArrayOf(android.R.attr.windowBackground))
            setBackgroundColor(array.getColor(0, 0))
            array.recycle()
            wrappedContext
                .layoutInflater
                .inflate(R.layout.view_app_theme, this, true)
            findViewById<TextView>(R.id.text_view).text = value.name
        }

    init {
        orientation = HORIZONTAL
        val p = round(resources.displayMetrics.density * 8f)
        setPadding(p, p, p, p)
    }
}