package com.lelloman.simplerss.persistence.settings

import com.lelloman.common.viewmodel.BaseViewModel

fun BaseViewModel.clear() {
    val onCleared = BaseViewModel::class.java.getDeclaredMethod("onCleared")
    onCleared.isAccessible = true
    onCleared.invoke(this)
}