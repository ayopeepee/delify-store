package com.swmpire.delifyit.presentation.ui.main.auth.utils

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

object PasswordConfirmValidator {
    fun validate(
        passwordValid: TextInputEditText,
        passwordConfirm: TextInputEditText,
        layout: TextInputLayout
    ): Boolean {
        return if (passwordConfirm.text.toString().trim() == passwordValid.text.toString().trim()) {
            layout.error = null
            true
        } else {
            layout.error = "Пароли не совпадают"
            false
        }
    }
}