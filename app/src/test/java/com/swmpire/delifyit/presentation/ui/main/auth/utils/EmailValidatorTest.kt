package com.swmpire.delifyit.presentation.ui.main.auth.utils

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class EmailValidatorTest {

    private val mockText: TextInputEditText = mockk(relaxed = true)
    private val mockLayout: TextInputLayout = mockk(relaxed = true)
    @Test
    fun `test correct email validation`() {

        val email = "correct@email.com"
        every { mockText.text.toString() } returns email

        val actual = EmailValidator.validate(mockText, mockLayout)

        assertThat(actual).isTrue()
    }

    @Test
    fun `test incorrect (empty) email validation`() {

        val email = ""
        every { mockText.text.toString() } returns email

        val actual = EmailValidator.validate(mockText, mockLayout)

        assertThat(actual).isFalse()
    }

    @Test
    fun `test incorrect (bad format) email validation`() {

        val email = "asd...123"
        every { mockText.text.toString() } returns email

        val actual = EmailValidator.validate(mockText, mockLayout)

        assertThat(actual).isFalse()
    }
}