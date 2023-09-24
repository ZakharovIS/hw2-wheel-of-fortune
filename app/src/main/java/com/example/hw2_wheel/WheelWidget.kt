package com.example.hw2_wheel

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.example.hw2_wheel.databinding.WheelWidgetBinding

class WheelWidget
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    val binding = WheelWidgetBinding.inflate(LayoutInflater.from(context))

    init {
        addView(binding.root)
    }
}