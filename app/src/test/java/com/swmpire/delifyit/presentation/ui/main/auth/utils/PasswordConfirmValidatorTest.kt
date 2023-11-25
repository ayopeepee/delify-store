package com.swmpire.delifyit.presentation.ui.main.auth.utils

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class PasswordConfirmValidatorTest {

    private val mockText: TextInputEditText = mockk(relaxed = true)
    private val mockConfirm: TextInputEditText = mockk(relaxed = true)
    private val mockLayout: TextInputLayout = mockk(relaxed = true)

    @Test
    fun `test correct password confirm (passwords equal)`() {
        // arrange
        val password = "123456"
        val passwordConfirm = "123456"
        every { mockText.text.toString() } returns password
        every { mockConfirm.text.toString() } returns passwordConfirm

        // act
        val actual = PasswordConfirmValidator.validate(mockText, mockConfirm, mockLayout)

        // assert
        assertThat(actual).isTrue()
    }

    @Test
    fun `test incorrect password confirm (passwords not equal)`() {
        // arrange
        val password = "123456"
        val passwordConfirm = "1234566789"
        every { mockText.text.toString() } returns password
        every { mockConfirm.text.toString() } returns passwordConfirm

        // act
        val actual = PasswordConfirmValidator.validate(mockText, mockConfirm, mockLayout)

        // assert
        assertThat(actual).isFalse()
    }
}