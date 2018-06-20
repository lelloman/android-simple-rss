package com.lelloman.read.core.view

import android.widget.Toast

class ToastEvent(
    val message: String,
    val duration: Int = Toast.LENGTH_SHORT) : ViewActionEvent