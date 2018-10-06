package com.lelloman.common.view.actionevent

import android.support.annotation.IntRange

class PickFileActionEvent(
    @IntRange(from = 0, to = 0xff) val requestCode: Int
) : ViewActionEvent