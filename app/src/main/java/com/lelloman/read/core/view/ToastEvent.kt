package com.lelloman.read.core.view

import android.support.annotation.StringRes
import android.widget.Toast

class ToastEvent(
    @StringRes val stringId: Int,
    val args: Array<Any> = emptyArray(),
    val duration: Int = Toast.LENGTH_SHORT) : ViewActionEvent