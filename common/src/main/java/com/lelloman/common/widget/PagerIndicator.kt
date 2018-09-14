package com.lelloman.common.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View
import com.lelloman.common.R

/**
 * Doesn't support padding -_-'
 */
class PagerIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes), ViewPager.OnPageChangeListener {

    var viewPager: ViewPager? = null
        set(value) {
            if (field != value) {
                onViewPagerSet(field, value)
                field = value
            }
        }

    private val emptyColor: Int
    private val fillColor: Int

    private val emptyPaint: Paint
    private val fillPaint: Paint

    private val circleRadius: Int
    private val circleMargin: Int
    private val circleRadiusAndMargin: Int
    private val circleRadiusAndMarginTimes2: Int

    private var emptyCirclesBitmap: Bitmap? = null
    private var circlesRect: Rect? = null
    private var filledCircleBitmap: Bitmap? = null
    private var filledCircleSrcRect = Rect(0, 0, 0, 0)
    private var filledCircleDstRect = Rect(0, 0, 0, 0)

    private var nIndicators = 0
    private var selectedIndicator = 0
    private var offset = 0f

    init {

        if (isInEditMode) {
            nIndicators = 4
        }

        val density = context.resources.displayMetrics.density
        val defaultCircleRadius = Math.round(density * DEFAULT_CIRCLE_RADIUS_DP)
        val defaultCircleMargin = Math.round(density * DEFAULT_CIRCLE_MARGIN_DP)

        if (attrs != null) {
            val a = context.obtainStyledAttributes(
                attrs,
                R.styleable.PagerIndicator,
                0,
                0
            )

            try {
                fillColor = a.getColor(R.styleable.PagerIndicator_indicatorFillColor, DEFAULT_FILL_COLOR)
                emptyColor = a.getColor(R.styleable.PagerIndicator_indicatorEmptyColor, DEFAULT_FILL_COLOR)
                circleRadius = a.getDimensionPixelSize(R.styleable.PagerIndicator_indicatorRadius, defaultCircleRadius)
                circleMargin = a.getDimensionPixelSize(R.styleable.PagerIndicator_indicatorMargin, defaultCircleMargin)
            } finally {
                a.recycle()
            }
        } else {
            circleRadius = defaultCircleRadius
            circleMargin = defaultCircleMargin
            fillColor = DEFAULT_FILL_COLOR
            emptyColor = DEFAULT_EMPTY_COLOR
        }

        circleRadiusAndMargin = circleRadius + circleMargin
        circleRadiusAndMarginTimes2 = circleRadiusAndMargin * 2

        emptyPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = emptyColor
        }
        fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = fillColor
        }
    }

    private fun onViewPagerSet(oldPager: ViewPager?, newPager: ViewPager?) {
        oldPager?.removeOnPageChangeListener(this)
        newPager?.also {
            newPager.addOnPageChangeListener(this)
            nIndicators = newPager.adapter?.count ?: 0
            makeEmptyCirclesBitmap(width, height)
            selectedIndicator = newPager.currentItem
        }
        invalidate()
    }

    @SuppressLint("SwitchIntDef")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            else -> circleRadiusAndMarginTimes2
        }

        setMeasuredDimension(measuredWidth, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        makeEmptyCirclesBitmap(w, h)
    }

    private fun releaseBitmaps() {
        emptyCirclesBitmap?.recycle()
        emptyCirclesBitmap = null
        filledCircleBitmap?.recycle()
        filledCircleBitmap = null
    }

    private fun makeEmptyCirclesBitmap(width: Int, height: Int) {
        releaseBitmaps()

        if (nIndicators > 0 && width > 0 && height > 0) {
            val bitmapWidth = nIndicators * circleRadiusAndMarginTimes2
            val bitmapHeight = circleRadiusAndMarginTimes2
            val bitmapX = (width - bitmapWidth) / 2
            val bitmapY = height - bitmapHeight

            circlesRect = Rect(bitmapX, bitmapY, bitmapX + bitmapWidth, height)
            filledCircleSrcRect.top = 0
            filledCircleSrcRect.bottom = circleRadiusAndMarginTimes2
            filledCircleDstRect.top = bitmapY
            filledCircleDstRect.bottom = bitmapY + circleRadiusAndMarginTimes2

            emptyCirclesBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888)
            Canvas(emptyCirclesBitmap).apply {
                var x = circleRadiusAndMargin.toFloat()
                val y = bitmapHeight / 2f
                val radius = circleRadius.toFloat()
                for (i in 0 until nIndicators) {
                    drawCircle(x, y, radius, emptyPaint)
                    x += circleRadiusAndMarginTimes2
                }
            }

            filledCircleBitmap = Bitmap.createBitmap(circleRadiusAndMarginTimes2, circleRadiusAndMarginTimes2, Bitmap.Config.ARGB_8888)
            Canvas(filledCircleBitmap).apply {
                drawCircle(
                    circleRadiusAndMargin.toFloat(),
                    circleRadiusAndMargin.toFloat(),
                    circleRadius.toFloat(),
                    fillPaint
                )
            }
        }
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        selectedIndicator = position
        offset = positionOffset
        invalidate()
    }

    override fun onPageSelected(position: Int) {
        selectedIndicator = position
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        emptyCirclesBitmap?.apply {
            canvas.drawBitmap(this, null, circlesRect, null)

            var position = selectedIndicator
            val filledCircleInverseWidth = Math.round(offset * circleRadiusAndMarginTimes2)
            var filledCircleLeft = circlesRect!!.left + circleRadiusAndMarginTimes2 * position + filledCircleInverseWidth
            var filledCircleRight = circlesRect!!.left + circleRadiusAndMarginTimes2 * (position + 1)

            filledCircleSrcRect.left = filledCircleInverseWidth
            filledCircleSrcRect.right = circleRadiusAndMarginTimes2
            filledCircleDstRect.left = filledCircleLeft
            filledCircleDstRect.right = filledCircleRight

            canvas.drawBitmap(filledCircleBitmap, filledCircleSrcRect, filledCircleDstRect, null)

            if (offset != 0f) {
                position = selectedIndicator + 1
                val filledCircleWidth = Math.round(offset * circleRadiusAndMarginTimes2)
                filledCircleLeft = circlesRect!!.left + circleRadiusAndMarginTimes2 * position
                filledCircleRight = circlesRect!!.left + circleRadiusAndMarginTimes2 * position + filledCircleWidth

                filledCircleSrcRect.left = 0
                filledCircleSrcRect.right = filledCircleWidth
                filledCircleDstRect.left = filledCircleLeft
                filledCircleDstRect.right = filledCircleRight

                canvas.drawBitmap(filledCircleBitmap, filledCircleSrcRect, filledCircleDstRect, null)
            }
        }
    }

    private companion object {
        const val DEFAULT_CIRCLE_RADIUS_DP = 10
        const val DEFAULT_CIRCLE_MARGIN_DP = 5

        const val DEFAULT_EMPTY_COLOR = 0xffffffff.toInt()
        const val DEFAULT_FILL_COLOR = 0xff000000.toInt()
    }
}