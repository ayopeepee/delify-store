package com.swmpire.delifyit.utils

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

interface TextChangeWatcher {
    fun observe(text: TextInputEditText, layout: TextInputLayout)
}