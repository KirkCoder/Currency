package ru.kcoder.currency.presentation.base

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import ru.kcoder.currency.R

class ErrorLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.error_layout, this)
        orientation = VERTICAL
        gravity = Gravity.CENTER
    }

}