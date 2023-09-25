package com.example.hw2_wheel

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
import kotlin.random.Random

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
    val startAngle = angle
    var isSpinning = false
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

    enum class SectorColor(val color: Int, val isText: Boolean) {
        RED(0xffff0000.toInt(), isText = true),
        ORANGE(0xffffa500.toInt(), isText = false),
        YELLOW(0xffffff00.toInt(), isText = true),
        GREEN(0xff008000.toInt(), isText = false),
        LIGHTBLUE(0xffadd8e6.toInt(), isText = true),
        BLUE(0xff0000ff.toInt(), isText = false),
        PURPLE(0xff800080.toInt(), isText = true);

    }

    private var currentColor = SectorColor.RED

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
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

    fun pickColor(): SectorColor {
        val nextColor = Random.nextInt(0, 7)

        return SectorColor.values()[nextColor]
    }

    fun startAnim(nextColor: SectorColor) {
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

            Toast.makeText(context, "$currentColor $angle", Toast.LENGTH_SHORT).show()
        }
        //animator.repeatCount = 30
        animator.start()
        //


    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        spinWheel(event!!)

        return super.onTouchEvent(event)
    }

    private fun spinWheel(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startAnim(pickColor())
                true
            }

            else -> {
                false
            }
        }
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
        val widthText = textPaint.measureText(text)
        canvas.drawText(text, width / 2f, height / 2f + radius + 50f, textPaint)
    }

}