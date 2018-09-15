package com.lelloman.common.view.actionevent

import android.widget.Toast

data class ToastEvent(
    val message: String,
    val duration: Int = Toast.LENGTH_SHORT) : ViewActionEvent