package com.lelloman.common.settings.property

import android.content.SharedPreferences


fun SharedPreferences.booleanProperty(key: String, default: Boolean) =
    BooleanProperty(key, default, this)

class BooleanProperty(
    key: String,
    default: Boolean,
    prefs: SharedPreferences
) : PrefsProperty<Boolean>(key, default, prefs) {

    override fun getValueFromPrefs(prefs: SharedPreferences) = prefs.getBoolean(key, default)

    override fun putValueInPrefs(editor: SharedPreferences.Editor, value: Boolean) {
        editor.putBoolean(key, value)
    }

}