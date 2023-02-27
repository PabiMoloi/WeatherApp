package com.weatherapp.common

import java.text.SimpleDateFormat
import java.util.*

val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

fun removeTimeStamp(date: String?): Date {
    val c = Calendar.getInstance(Locale.getDefault())
    c.time = dateFormatter.parse(date) as Date
    c.add(Calendar.MONTH, 1)
    return c.time
}
