package com.berkah.alfamet.util

import android.content.Context
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.AppCompatEditText
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import com.berkah.alfamet.R
import kotlinx.android.synthetic.main.bottom_sheet_confirmation_dialog.*
import java.math.BigDecimal
import java.text.DecimalFormat


fun View.visible() {
    visibility = View.VISIBLE
}

fun View.inVisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun BigDecimal.toCurr(): String {
    val formatter = DecimalFormat("#,##0;-#,##0")
    return formatter.format(this)
}

fun AppCompatEditText.onChange(listener: (String) -> Unit) {
    addTextChangedListener(object : TextWatcher {

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            listener(s.toString())
        }
    })
}

fun AppCompatEditText.value(): String = text.toString()

fun Context.bottomSheetConfirmationDialog(message: String, yesListener: () -> Unit) {
    BottomSheetDialog(this).apply {
        val contentView =
            LayoutInflater.from(this@bottomSheetConfirmationDialog)
                .inflate(R.layout.bottom_sheet_confirmation_dialog, null)
        setContentView(contentView)

        tvMessage.text = message

        btnYes.setOnClickListener {
            yesListener()
            hide()
        }

        btnNo.setOnClickListener { hide() }

        show()
    }
}
