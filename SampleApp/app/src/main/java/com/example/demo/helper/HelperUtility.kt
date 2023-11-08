package com.example.demo.helper

import android.text.TextUtils
import java.util.Locale

object HelperUtility {
    const val CONSECUTIVE_WHITESPACE_REGEX = "\\s+"
    fun getFirstLastCharName(fullName: String): String {
        var fullCharName = ""
        try {
            val parts: Array<String> = fullName.split(CONSECUTIVE_WHITESPACE_REGEX.toRegex()).toTypedArray()
            var lastChar = ""
            val firstChar = if (parts.isNotEmpty() && !parts[0].equals("", ignoreCase = true)) {
                getEmoteFreeInitial(parts[0]).toString()
            } else ""
            if (parts.size > 1 && !parts[1].equals("", ignoreCase = true)) {
                lastChar = parts[1].substring(0, 1)
                lastChar = if (lastChar == "(") {
                    ""
                } else {
                    getEmoteFreeInitial(parts[1]).toString()
                }
            } else {
                lastChar = parts[0].substring(1,2)
            }
            fullCharName = firstChar + lastChar
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return fullCharName.uppercase(Locale.getDefault())
    }

    private fun getEmoteFreeInitial(emoteString: String): String? {
        if (TextUtils.isEmpty(emoteString)) return ""
        val emoteFreeString = StringBuilder()
        val ch = emoteString[0]
        if (!Character.isHighSurrogate(ch)) emoteFreeString.append(ch)
        return emoteFreeString.toString()
    }
}