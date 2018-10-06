package com.lelloman.common.view.actionevent

import java.io.File

class ShareFileViewActionEvent(
    val file: File,
    val authority: String
) : ViewActionEvent