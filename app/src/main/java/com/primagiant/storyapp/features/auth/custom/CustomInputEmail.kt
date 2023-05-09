package com.primagiant.storyapp.features.auth.custom

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.primagiant.storyapp.R

class CustomInputEmail : AppCompatEditText {

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

    private fun init() {

        errorBackground =
            ContextCompat.getDrawable(context, R.drawable.err_input_border) as Drawable
        normalBackground =
            ContextCompat.getDrawable(context, R.drawable.input_border) as Drawable

        maxLines = 1

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not used
            }

            override fun afterTextChanged(s: Editable?) {
                validateEmail(s.toString())
                background = if (isEmailValid(s.toString())) {
                    normalBackground
                } else errorBackground
            }
        })
    }

    private fun validateEmail(email: String) {
        val isValid = isEmailValid(email)

        errorText = if (isValid) {
            null
        } else {
            context.getString(R.string.error_email)
        }

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

    private fun isEmailValid(email: String): Boolean {
        return Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$").matches(email)
    }
}