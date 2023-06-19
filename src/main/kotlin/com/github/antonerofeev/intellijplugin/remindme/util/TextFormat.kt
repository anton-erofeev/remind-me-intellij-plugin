package com.github.antonerofeev.intellijplugin.remindme.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

internal fun Long.formatTimestamp(style: FormatStyle): String {
    val formatter: DateTimeFormatter =
        DateTimeFormatter.ofLocalizedDateTime(style)
            .withLocale(Locale.getDefault())
            .withZone(ZoneId.systemDefault())
    val instant: Instant = Instant.ofEpochMilli(this)
    return formatter.format(instant)
}