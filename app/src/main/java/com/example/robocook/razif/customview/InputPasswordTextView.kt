package com.example.robocook.razif.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText

class InputPasswordTextView : AppCompatEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {

        addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { error = "Your password should at least be 8 characters long" }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { if (s?.length!! >= 8) else error = "Your password should at least be 8 characters long" }
            override fun afterTextChanged(s: Editable) { if (s?.length!! >= 8) else error = "Your password should at least be 8 characters long" }

        })

    }

    override fun onDraw(canvas: Canvas) {

        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START

    }

}