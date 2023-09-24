package com.example.hw2_wheel

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class WheelView
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val radius: Float = 150f
    private var center = PointF()
    private var rectF = RectF()
    private var angle = 0f
    private val strokePaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }
    private val fillPaint = Paint().apply {
        style = Paint.Style.FILL
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 16f
        color = Color.BLACK
    }

    private enum class SectorColor(val color: Int, val isText: Boolean) {
        RED(0xffff0000.toInt(), isText = true),
        ORANGE(0xffffa500.toInt(), isText = false),
        YELLOW(0xffffff00.toInt(), isText = true),
        GREEN(0xff008000.toInt(), isText = false),
        LIGHTBLUE(0xffadd8e6.toInt(), isText = true),
        BLUE(0xff0000ff.toInt(), isText = false),
        PURPLE(0xff800080.toInt(), isText = true)
    }

    private var currentColor = SectorColor.RED


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        center = PointF(width / 2f, height / 2f)
        rectF = RectF(center.x - radius, center.y - radius, center.x + radius, center.y + radius)
    }

    override fun onDraw(canvas: Canvas) {

        drawWheel(canvas)
        super.onDraw(canvas)


    }

    fun startAnim() {

        val startAngle = angle
        val animator = ValueAnimator.ofFloat(startAngle, 360f / 7 * 6 + startAngle + 360f)
        animator.duration = 1000
        animator.addUpdateListener {
            angle = it.animatedValue as Float
            invalidate()
        }
        //animator.repeatCount = 30
        animator.start()
        currentColor = SectorColor.values()[(currentColor.ordinal + 1) % 7]


    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        spinWheel(event!!)
        return super.onTouchEvent(event)
    }

    private fun spinWheel(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startAnim()
                true
            }

            else -> {
                false
            }
        }
    }

    fun drawWheel(canvas: Canvas) {
        canvas.drawCircle(width / 2f, height / 2f, radius, strokePaint)
        //Toast.makeText(context, "$rectF", Toast.LENGTH_LONG).show()
        enumValues<SectorColor>().forEach {
            fillPaint.color = SectorColor.values()[(it.ordinal + currentColor.ordinal) % 7].color

            canvas.drawArc(
                rectF,
                ((((-360f + 720f * it.ordinal) / 7) / 2) + angle),
                (((360f) / 7)),
                true,
                fillPaint
            )
        }

        /*val animation = RotateAnimation(0f, 360f, width / 2f, height / 2f)
        animation.duration = 2500
        animation.fillAfter
        this.startAnimation(animation)*/
    }

}