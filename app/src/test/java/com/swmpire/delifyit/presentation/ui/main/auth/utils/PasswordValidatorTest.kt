package com.swmpire.delifyit.presentation.ui.main.auth.utils

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class PasswordValidatorTest {

    private val mockText: TextInputEditText = mockk(relaxed = true)
    private val mockLayout: TextInputLayout = mockk(relaxed = true)

    @Test
    fun `test correct password validation`() {

        val password = "123456"
        every { mockText.text.toString() } returns password

        val actual = PasswordValidator.validate(mockText, mockLayout)

        assertThat(actual).isTrue()
    }

    @Test
    fun `test incorrect (empty) password validation`() {

        val password = ""
        every { mockText.text.toString() } returns password

        val actual = PasswordValidator.validate(mockText, mockLayout)

        assertThat(actual).isFalse()
    }

    @Test
    fun `test incorrect (contains spaces) password validation`() {

        val password = " "
        every { mockText.text.toString() } returns password

        val actual = PasswordValidator.validate(mockText, mockLayout)

        assertThat(actual).isFalse()
    }

    @Test
    fun `test incorrect (less than 6 symbols) password validation`() {

        val password = "123"
        every { mockText.text.toString() } returns password

        val actual = PasswordValidator.validate(mockText, mockLayout)

        assertThat(actual).isFalse()
    }

    @Test
    fun `test incorrect (more that 20 symbols) password validation`() {

        val password = "12345678912345678912345"
        every { mockText.text.toString() } returns password

        val actual = PasswordValidator.validate(mockText, mockLayout)

        assertThat(actual).isFalse()
    }
}