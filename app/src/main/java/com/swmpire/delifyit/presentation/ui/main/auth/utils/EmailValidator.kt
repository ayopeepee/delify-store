package com.swmpire.delifyit.presentation.ui.main.auth.utils

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import android.util.Patterns
import androidx.core.util.PatternsCompat

object EmailValidator {
    fun validate(text: TextInputEditText, layout: TextInputLayout): Boolean {
        with(text.text.toString().trim()) {
            return when {
                isEmpty() -> {
                    layout.error = "Введите email"
                    false
                }

                !PatternsCompat.EMAIL_ADDRESS.matcher(this).matches() -> {
                    layout.error = "Некорректный формат email"
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