package com.example.hw2_wheel

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.animation.doOnEnd
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.Slider
import kotlin.random.Random

class WheelWidget
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    val wheelView = WheelView(context, attrs, defStyleAttr)
    val imageView = ImageView(context, attrs, defStyleAttr)
    val slider = Slider(context, attrs, defStyleAttr)
    val button = MaterialButton(context, attrs, defStyleAttr)

    private var isSpinning = false

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(500, 1150)
    }

    override fun onAttachedToWindow() {
        this.addView(wheelView)
        this.addView(imageView)
        this.addView(button)
        this.addView(slider)

        setViews()

        wheelView.setOnClickListener {
            if(!isSpinning) rotateWheel(pickColor())
        }
        button.setOnClickListener {

            if (!isSpinning) {
                imageView.setImageDrawable(null)
                wheelView.reset()
            }
        }

        super.onAttachedToWindow()
    }

    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
        wheelView.layout(0, 0, 500, 500)
        slider.layout(0, 550, 500, 650)
        imageView.layout(0, 700, 700, 1000)
        button.layout(100, 1050, 400, 1150)
    }

    private fun setViews() {
        button.text = "RESET"
        button.setPadding(20, 20, 20, 20)
        button.gravity = Gravity.CENTER
        button.includeFontPadding = true
        slider.valueFrom = 1f
        slider.valueTo = 100f
        slider.value = 50f
    }

    private fun pickColor(): SectorColor {
        val nextColor = Random.nextInt(0, 7)
        return SectorColor.values()[nextColor]
    }

    private fun rotateWheel(nextColor: SectorColor) {
        isSpinning = true
        val animator = wheelView.startAnim(nextColor)
        animator.doOnEnd {
            if (!wheelView.currentColor.isText) {
                Glide
                    .with(this)
                    .load("https://random.imagecdn.app/300/500")
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(imageView)
            } else {
                imageView.setImageDrawable(null)
            }
            isSpinning = false
        }
    }


}