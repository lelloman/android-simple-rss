package com.lelloman.read.persistence.settings.property

import android.content.SharedPreferences
import com.lelloman.read.utils.Named

fun <T : Named> SharedPreferences.enumProperty(
    key: String,
    default: T,
    stringToEnum: (String) -> T
) = EnumProperty(
    key = key,
    default = default,
    stringToEnum = stringToEnum,
    prefs = this
)

class EnumProperty<T : Named>(
    key: String,
    default: T,
    prefs: SharedPreferences,
    private val stringToEnum: (String) -> T
) : PrefsProperty<T>(key, default, prefs) {

    override fun getValueFromPrefs(prefs: SharedPreferences): T =
        stringToEnum.invoke(prefs.getString(key, default.name))

    override fun putValueInPrefs(editor: SharedPreferences.Editor, value: T) {
        editor.putString(key, value.name)
    }
}