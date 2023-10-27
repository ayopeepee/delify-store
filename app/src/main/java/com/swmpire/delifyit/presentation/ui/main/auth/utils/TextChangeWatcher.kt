package com.swmpire.delifyit.presentation.ui.main.auth.utils

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

interface TextChangeWatcher {
    fun observe(text: TextInputEditText, layout: TextInputLayout)
}