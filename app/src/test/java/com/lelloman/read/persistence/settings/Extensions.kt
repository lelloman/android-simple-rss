package com.lelloman.read.persistence.settings

import com.lelloman.read.core.viewmodel.BaseViewModel

fun BaseViewModel.clear() {
    val onCleared = BaseViewModel::class.java.getDeclaredMethod("onCleared")
    onCleared.isAccessible = true
    onCleared.invoke(this)
}