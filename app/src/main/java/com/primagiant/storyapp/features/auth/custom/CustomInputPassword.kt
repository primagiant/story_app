package com.primagiant.storyapp.features.auth.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.primagiant.storyapp.R

class CustomInputPassword : AppCompatEditText, View.OnTouchListener {

    private lateinit var eyeIcon: Drawable
    private lateinit var lockIcon: Drawable

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
        eyeIcon = ContextCompat.getDrawable(context, R.drawable.ic_eye_24) as Drawable
        lockIcon = ContextCompat.getDrawable(context, R.drawable.ic_eye_24) as Drawable

        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(c: CharSequence?, start: Int, end: Int, count: Int) {
                if (c.toString().isNotEmpty()) showPassword() else hidePassword()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    override fun onTouch(v: View?, e: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            var showPasswordStart: Float
            var showPasswordEnd: Float
            var isShowPasswordButtonClicked = false

            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                showPasswordEnd = (eyeIcon.intrinsicWidth + paddingStart).toFloat()
                when {
                    e.x < showPasswordEnd -> isShowPasswordButtonClicked = true
                }
            } else {
                showPasswordStart = (width - paddingEnd - eyeIcon.intrinsicWidth).toFloat()
                when {
                    e.x > showPasswordStart -> isShowPasswordButtonClicked = true
                }
            }

            if (isShowPasswordButtonClicked) {
                when (e.action) {
                    MotionEvent.ACTION_DOWN -> {
                        this.transformationMethod = HideReturnsTransformationMethod.getInstance()
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        this.transformationMethod = PasswordTransformationMethod.getInstance()
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun showPassword() {
        setButtonDrawable(endOfTheText = eyeIcon)
    }

    private fun hidePassword() {
        setButtonDrawable()
    }

    private fun setButtonDrawable(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null,
    ) {
        setCompoundDrawablesRelativeWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    fun isPasswordValid(password: String): Boolean {
        val passwordRegex = Regex("^(?=.*[A-Z])(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?])(?=\\S+\$).{8,}\$")
        return passwordRegex.matches(password)
    }

    private fun isEightDigit(password: String): Boolean {
        return password.length >= 8
    }
}