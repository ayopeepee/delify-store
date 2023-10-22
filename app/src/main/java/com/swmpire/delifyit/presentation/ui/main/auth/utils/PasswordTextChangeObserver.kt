package com.swmpire.delifyit.presentation.ui.main.auth.utils

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class PasswordTextChangeObserver(private val validator: PasswordValidator) : TextChangeWatcher {
    override fun observe(text: TextInputEditText, layout: TextInputLayout) {
        text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                validator.validate(text, layout)
            }
        })
    }
}