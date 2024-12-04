package com.capstone.aiskin.core.helper

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateTimeConverter {

    fun formatTimestamp(timestamp: Long, format: String = "dd MMM yyyy, HH:mm:ss"): String {
        val date = Date(timestamp)
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        return dateFormat.format(date)
    }

    fun formatFirestoreTimestamp(seconds: Long, nanoseconds: Int, format: String = "dd MMM yyyy, HH:mm:ss"): String {
        val milliseconds = seconds * 1000 + nanoseconds / 1_000_000
        val date = Date(milliseconds)
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        return dateFormat.format(date)
    }
}