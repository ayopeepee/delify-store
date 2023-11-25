package com.swmpire.delifyit.presentation.ui.main.tabs.utils

import android.content.Context
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class TextValidatorTest {

    private val mockText: TextInputEditText = mockk(relaxed = true)
    private val mockLayout: TextInputLayout = mockk(relaxed = true)
    private val mockContext: Context = mockk(relaxed = true)

    @Test
    fun `test correct text validation`() {
        // arrange
        val text = "some text"
        every { mockText.text.toString() } returns text

        // act
        val actual = TextValidator(mockContext).validate(mockText, mockLayout)

        // assert
        assertThat(actual).isTrue()
    }

    @Test
    fun `test incorrect (empty) text validation`() {
        // arrange
        val text = " "
        every { mockText.text.toString() } returns text

        // act
        val actual = TextValidator(mockContext).validate(mockText, mockLayout)

        // assert
        assertThat(actual).isFalse()
    }

    @Test
    fun `test incorrect (invalid symbols) text validation`() {
        // arrange
        val text = "%incorrect#*%text"
        every { mockText.text.toString() } returns text

        // act
        val actual = TextValidator(mockContext).validate(mockText, mockLayout)

        // assert
        assertThat(actual).isFalse()
    }
}