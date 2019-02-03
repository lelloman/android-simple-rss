package com.lelloman.simplerss.widget

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.SparseArray
import android.widget.CompoundButton
import android.widget.Switch
import com.lelloman.simplerss.R


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

    override fun onSaveInstanceState(): Parcelable? = Bundle().apply {
        putParcelable(KEY_SUPER_STATE, super.onSaveInstanceState())
        putBoolean(KEY_IS_CHECKED, switch.isChecked)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val superState = if (state is Bundle) {
            switch.isChecked = state.getBoolean(KEY_IS_CHECKED)
            state.getParcelable(KEY_SUPER_STATE)
        } else {
            state
        }
        super.onRestoreInstanceState(superState)
    }

    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable>?) {
        // no op
    }

    private companion object {
        const val KEY_SUPER_STATE = "SuperState"
        const val KEY_IS_CHECKED = "IsChecked"
    }
}