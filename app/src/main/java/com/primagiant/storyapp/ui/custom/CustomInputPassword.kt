package com.primagiant.storyapp.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.primagiant.storyapp.R

// TODO: need to be fixed
class CustomInputPassword : AppCompatEditText, View.OnTouchListener {


    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    private fun init() {
        val eyeIcon = ContextCompat.getDrawable(context, R.drawable.baseline_eye_24)

        setOnTouchListener(this)
    }

    override fun onTouch(v: View?, e: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            var isShowPasswordButtonClicked = false

            if (isShowPasswordButtonClicked) {
                when (e.action) {
                    MotionEvent.ACTION_BUTTON_PRESS -> {
                        // tampilkan gambar

                        // check text kosong tidak
                    }
                }
            }
        }
        return false
    }
}