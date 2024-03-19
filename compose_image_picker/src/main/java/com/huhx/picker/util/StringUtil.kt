package com.huhx.picker.util

object StringUtil {

    private const val ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm"
    private const val NUMBER_CHARACTERS = "0123456789"

    fun randomString(count: Int): String {
        val stringBuilder = StringBuilder(count)
        repeat(count) {
            stringBuilder.append(ALLOWED_CHARACTERS.random())
        }
        return stringBuilder.toString()
    }

    fun randomNumeric(count: Int): String {
        val stringBuilder = StringBuilder(count)
        repeat(count) {
            stringBuilder.append(NUMBER_CHARACTERS.random())
        }
        return stringBuilder.toString()
    }
}