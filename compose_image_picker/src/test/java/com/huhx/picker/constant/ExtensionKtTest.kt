package com.huhx.picker.constant

import org.junit.Assert.assertEquals
import org.junit.Test

class ExtensionKtTest {

    @Test
    fun `should add prefix zero given value is less than ten`() {
        val result = 9L.prefixZero()

        assertEquals("09", result)
    }

    @Test
    fun `should not add prefix zero given value is greater or equal ten`() {
        val result = 10L.prefixZero()

        assertEquals("10", result)
    }
}