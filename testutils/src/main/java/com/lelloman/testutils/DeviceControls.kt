package com.lelloman.testutils

import android.support.test.InstrumentationRegistry
import android.support.test.uiautomator.UiDevice

fun rotateNatural() = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).setOrientationNatural()

fun rotateLeft() = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).setOrientationLeft()

fun rotateRight() = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).setOrientationRight()
