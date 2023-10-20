package com.swmpire.delifyit.presentation.ui.main.auth.utils

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout



object EmailValidator {
    fun validate(text: TextInputEditText, layout: TextInputLayout) : Boolean {
        with(text.text.toString().trim()) {
            return when {
                isEmpty() -> {
                    layout.error = "Введите пароль"
                    false
                }
                contains(" ") -> {
                    layout.error = "Пароль не должен содержать пробелы"
                    false
                }
                length < 6 -> {
                    layout.error = "Минимум 6 символов"
                    false
                }
                length > 20 -> {
                    layout.error = "Максимум 20 символов"
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