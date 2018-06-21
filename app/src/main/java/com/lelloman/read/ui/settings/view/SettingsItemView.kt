package com.lelloman.read.ui.settings.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.lelloman.read.R

class SettingsItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val textViewTitle by lazy { findViewById<TextView>(R.id.text_view_title) }
    private val textViewDescription by lazy { findViewById<TextView>(R.id.text_view_description) }

    init {
        View.inflate(context, R.layout.view_settings_item, this)
        orientation = VERTICAL

        attrs?.let {
            val a = context.obtainStyledAttributes(
                attrs,
                R.styleable.SettingsItemView,
                0,
                0
            )

            try {
                textViewTitle.text = a.getString(R.styleable.SettingsItemView_title)
                textViewDescription.text = a.getString(R.styleable.SettingsItemView_description)
            } finally {
                a.recycle()
            }
        }
    }
}