package com.lelloman.read.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.CompoundButton
import android.widget.Switch
import com.lelloman.read.R

class ToggleSettingItemView
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SettingsItemView(context, attrs, defStyleAttr) {

    private val switch by lazy { findViewById<Switch>(R.id.toggle_button) }

    override fun getLayoutId() = R.layout.view_toggle_setting_item

    fun setChecked(isChecked: Boolean) {
        switch.isChecked = isChecked
    }

    fun setOnCheckedChanged(listener: CompoundButton.OnCheckedChangeListener) {
        switch.setOnCheckedChangeListener(listener)
    }
}