package com.lelloman.testutils

import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.CoordinatesProvider
import android.support.test.espresso.action.GeneralSwipeAction
import android.support.test.espresso.action.Press
import android.support.test.espresso.action.Swipe


fun swipeBottomSheetUp(): ViewAction = GeneralSwipeAction(
    Swipe.FAST,
    CoordinatesProvider {
        floatArrayOf(200f, 1750f)
    },
    CoordinatesProvider {
        floatArrayOf(400f, 700f)
    },
    Press.FINGER
)