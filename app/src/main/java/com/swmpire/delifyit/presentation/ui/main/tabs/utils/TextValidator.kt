package com.swmpire.delifyit.presentation.ui.main.tabs.utils

import android.content.Context
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.swmpire.delifyit.R

class TextValidator(private val context: Context) {

    fun validate(text: TextInputEditText, layout: TextInputLayout): Boolean {
        with(text.text.toString().trim()) {
            return when {
                isBlank() -> {
                    layout.error = context.getString(R.string.empty_field)
                    false
                }
                "[~@#\$%^&*()_\\-+=\\[\\]{};:'\"/<>]".toRegex().containsMatchIn(this) -> {
                    layout.error = context.getString(R.string.invalid_characters)
                    false
                }
                else -> {
                    layout.error = null
                    true
                }
            }
        }
    }
}