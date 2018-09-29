package com.lelloman.common.widget.bottomdrawer

import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.widget.FrameLayout

class BottomDrawerFrame(context: Context)
    : FrameLayout(context), View.OnTouchListener, SlideAnimation.Listener {

    interface BottomDrawerFrameContent {
        val headerHeight: Int
        fun setVerticalOffset(verticalOffset: Float)
        fun onStateChanged(state: BottomDrawerLayout.State)
    }

    private var verticalOffset = 0f
    private var isAnimatingInternal: Boolean = false

    var isWaitingForUpEvent: Boolean = false
        private set

    private val velocityTracker by lazy { VelocityTracker.obtain() }
    private var touchDownY = 0f
    private var touchDownMs = 0L
    private var state: BottomDrawerLayout.State = BottomDrawerLayout.State.CLOSED

    private lateinit var contentInterface: BottomDrawerFrameContent
    private var headerOffset = 0f

    init {
        setOnTouchListener(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount != 1) {
            throw IllegalStateException()
        }
        contentInterface = getChildAt(0) as? BottomDrawerFrameContent
            ?: throw java.lang.IllegalStateException()
    }

    fun addContentView(contentView: View) {
        contentInterface = contentView as? BottomDrawerFrameContent
            ?: throw IllegalStateException()
        addView(contentView)
    }

    override fun setAnimating(animating: Boolean) {
        isAnimatingInternal = animating
    }

    private fun eventUp(event: MotionEvent) {
        isWaitingForUpEvent = false
        event.setLocation(event.x, event.y - verticalOffset)
        velocityTracker.addMovement(event)
        velocityTracker.computeCurrentVelocity(1000)

        val index = event.actionIndex
        val pointerId = event.getPointerId(index)
        val velocityY = velocityTracker.getYVelocity(pointerId)

        val kv = verticalOffset / (height - contentInterface.headerHeight)//1 - Math.min(Math.abs(velocityY / 3000.0), 1.0)
        val animationTime = (MIN_ANIMATION_TIME + DELTA_ANIMATION_TIME * kv).toInt()
        when {
            velocityY > SWIPE_VELOCITY_THRESHOLD || velocityY as Number == Float.NaN -> {
                setState(BottomDrawerLayout.State.MINI, true, animationTime)
            }
            velocityY < -SWIPE_VELOCITY_THRESHOLD -> setState(BottomDrawerLayout.State.FULL, true, animationTime)
            verticalOffset > height / 2 -> setState(BottomDrawerLayout.State.MINI, true, animationTime)
            else -> setState(BottomDrawerLayout.State.FULL, true, animationTime)
        }
    }

    fun getState() = state

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        headerOffset = (h - contentInterface.headerHeight).toFloat()
        setOffsetForState(h)
    }

    private fun setOffsetForState(viewHeight: Int) = when (state) {
        BottomDrawerLayout.State.CLOSED -> setVerticalOffset(viewHeight.toFloat())
        BottomDrawerLayout.State.FULL -> setVerticalOffset(0f)
        BottomDrawerLayout.State.MINI -> setVerticalOffset(headerOffset)
    }

    @JvmOverloads
    fun setState(state: BottomDrawerLayout.State, animate: Boolean = false, animationTime: Int = 0) {
        this.state = state
        contentInterface.onStateChanged(state)
        if (!animate) {
            setOffsetForState(height)
        } else {
            val targetOffset = when (state) {
                BottomDrawerLayout.State.CLOSED -> height
                BottomDrawerLayout.State.MINI -> headerOffset.toInt()
                BottomDrawerLayout.State.FULL -> 0
            }
            startAnimation(SlideAnimation(animationTime.toLong(), verticalOffset, targetOffset.toFloat(), this))
        }
    }

    override fun setVerticalOffset(verticalOffset: Float) {
        this.verticalOffset = verticalOffset
        contentInterface.setVerticalOffset(verticalOffset)
        postInvalidate()
    }

    fun getVerticalOffset() = verticalOffset

    override fun dispatchDraw(canvas: Canvas) {
        canvas.save()
        canvas.translate(0f, verticalOffset)
        super.dispatchDraw(canvas)
        canvas.restore()
    }

    private fun handleActionDown(event: MotionEvent): Boolean {
        touchDownMs = System.currentTimeMillis()
        touchDownY = event.y
        velocityTracker.clear()
        velocityTracker.addMovement(event)
        isWaitingForUpEvent = true
        return true
    }

    private fun handleActionUpOrCancel(event: MotionEvent) = isWaitingForUpEvent.apply {
        eventUp(event)
    }

    private fun handleActionMove(event: MotionEvent) = if (!isWaitingForUpEvent) {
        false
    } else {

        val offset = event.y - touchDownY + verticalOffset
        setVerticalOffset(offset)

        event.setLocation(event.x, event.y - verticalOffset)
        velocityTracker.addMovement(event)

        if (verticalOffset < 0) {
            setVerticalOffset(0f)
        } else if (verticalOffset > headerOffset) {
            verticalOffset = headerOffset
        }
        true
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (isAnimatingInternal)
            return false

        val action = event.action

        return when (action) {
            MotionEvent.ACTION_DOWN -> handleActionDown(event)
            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_UP -> handleActionUpOrCancel(event)
            MotionEvent.ACTION_MOVE -> handleActionMove(event)
            else -> return false
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val originalY = ev.y

        ev.setLocation(ev.x, ev.y - verticalOffset)
        val handled = super.dispatchTouchEvent(ev)
        if (!handled) {
            ev.setLocation(ev.x, originalY)
        }
        return handled
    }

    fun setVisible(visible: Boolean) {
        if (visible && state == BottomDrawerLayout.State.CLOSED) {
            setState(BottomDrawerLayout.State.MINI)
            post { requestLayout() }
        } else if (!visible) {
            setState(BottomDrawerLayout.State.CLOSED)
        }
    }

    companion object {
        const val SWIPE_VELOCITY_THRESHOLD = 1000
        const val MIN_ANIMATION_TIME = 80
        const val MAX_ANIMATION_TIME = 380
        const val DELTA_ANIMATION_TIME = MAX_ANIMATION_TIME - MIN_ANIMATION_TIME
    }
}