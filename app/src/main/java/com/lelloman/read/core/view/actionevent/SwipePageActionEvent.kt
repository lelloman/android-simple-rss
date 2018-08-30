package com.lelloman.read.core.view.actionevent

class SwipePageActionEvent(val direction: Direction) : ViewActionEvent {

    enum class Direction { LEFT, RIGHT }
}