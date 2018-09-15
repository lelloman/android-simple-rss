package com.lelloman.common.view.actionevent

class SwipePageActionEvent(val direction: Direction) : ViewActionEvent {

    enum class Direction { LEFT, RIGHT }
}