package com.primagiant.storyapp.features.auth.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
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
    private lateinit var errorBackground: Drawable
    private lateinit var normalBackground: Drawable

    private var errorText: String? = null

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
        errorBackground =
            ContextCompat.getDrawable(context, R.drawable.err_input_border) as Drawable
        normalBackground =
            ContextCompat.getDrawable(context, R.drawable.input_border) as Drawable

        setOnTouchListener(this)
        maxLines = 1

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(c: CharSequence?, start: Int, end: Int, count: Int) {
                // Not used
            }

            override fun onTextChanged(c: CharSequence?, start: Int, end: Int, count: Int) {
                // Not used
            }

            override fun afterTextChanged(s: Editable?) {
                validatePassword(s.toString())
                if (s.toString().isNotEmpty()) showPassword() else hidePassword()
                background = if (s.toString().length < 8) {
                    normalBackground
                } else errorBackground
            }

        })
    }

    override fun onTouch(v: View?, e: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val showPasswordStart: Float
            val showPasswordEnd: Float
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

    private fun validatePassword(password: String) {
        // Validate the password according to your requirements
        val isValid = password.length >= 8

        // Set the error text if the password is invalid
        errorText = if (isValid) {
            null
        } else {
            context.getString(R.string.err_password)
        }

        // Show or hide the error text based on the validity
        showErrorText(isValid)
    }

    private fun showErrorText(isValid: Boolean) {
        if (errorText != null && !isValid) {
            error = errorText
            setTextColor(Color.RED)
        } else {
            error = null
            setTextColor(Color.BLACK)
        }
    }
}