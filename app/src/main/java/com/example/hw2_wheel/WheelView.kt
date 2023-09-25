package com.example.hw2_wheel

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.accessibility.AccessibilityEvent
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.animation.doOnEnd
import androidx.core.view.updateLayoutParams
import kotlin.random.Random

class WheelView
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val radius: Float = 200f
    private var center = PointF()
    private var rectF = RectF()
    private var angle = 0f

    private var defaultWidth = 500
    private var defaultHeight = 500

    private val strokePaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }
    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val fillPaintArrow = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.BLACK
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 50f
        color = Color.BLACK
        textAlign = Paint.Align.CENTER
    }
    private val pathArrow = Path().apply {
        moveTo(0f, 0f)
        lineTo(80f, -20f)
        lineTo(80f, 20f)
        close()
    }
    var currentColor = SectorColor.RED
        private set
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(defaultWidth, defaultHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        center = PointF(width / 2f, height / 2f)
        rectF = RectF(center.x - radius, center.y - radius, center.x + radius, center.y + radius)
        pathArrow.offset(width / 2f + radius * 7 / 8, height / 2f)
    }

    override fun onDraw(canvas: Canvas) {

        if (currentColor.isText) {
            drawText(canvas)
        }
        drawWheel(canvas)
        super.onDraw(canvas)

    }

    fun startAnim(nextColor: SectorColor): Animator {
        val spinsCount = Random.nextInt(1, 5)
        val animator = ValueAnimator.ofFloat(
            angle,
            (360f / SectorColor.values().size * (SectorColor.values().size - nextColor.ordinal)) + 360f * spinsCount
        )
        animator.duration = 2000
        animator.addUpdateListener {
            angle = it.animatedValue as Float
            invalidate()
        }
        animator.doOnEnd {

            currentColor = nextColor
            angle = 360f - (360f / SectorColor.values().size * nextColor.ordinal)

        }
        animator.start()
        return animator
    }

    fun drawWheel(canvas: Canvas) {
        canvas.drawCircle(width / 2f, height / 2f, radius, strokePaint)
        enumValues<SectorColor>().forEach {
            fillPaint.color = it.color
            canvas.drawArc(
                rectF,
                (((-180f + 360f * it.ordinal) / SectorColor.values().size) + angle),
                (((360f) / SectorColor.values().size)),
                true,
                fillPaint
            )
        }
        canvas.drawPath(pathArrow, fillPaintArrow)
    }
    fun drawText(canvas: Canvas) {
        val text = currentColor.name
        canvas.drawText(text, width / 2f, height / 2f + radius + 50f, textPaint)
    }

    fun reset() {
        currentColor = SectorColor.RED
        angle = 0f
        invalidate()
    }

    /*fun changeSize(newWidth: Int, newHeight: Int) {
        this.updateLayoutParams {
            defaultWidth = newWidth
            defaultHeight = newHeight
        }
        requestLayout()
    }*/

}