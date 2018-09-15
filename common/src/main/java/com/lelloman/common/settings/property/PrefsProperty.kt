package com.lelloman.common.settings.property

import android.content.SharedPreferences
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject

abstract class PrefsProperty<T>(
    protected val key: String,
    protected val default: T,
    private val prefs: SharedPreferences
) {

    private val subject: Subject<T> = BehaviorSubject.create()

    val observable: Observable<T> = subject.hide()

    protected abstract fun getValueFromPrefs(prefs: SharedPreferences): T

    protected abstract fun putValueInPrefs(editor: SharedPreferences.Editor, value: T)

    fun readValue() = subject.onNext(getValueFromPrefs(prefs))

    fun set(value: T) = prefs.edit().run {
        putValueInPrefs(this, value)
        apply()
        subject.onNext(value)
    }
}