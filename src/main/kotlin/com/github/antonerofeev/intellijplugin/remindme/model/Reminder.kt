package com.github.antonerofeev.intellijplugin.remindme.model

import com.github.antonerofeev.intellijplugin.remindme.enums.ReminderColor

internal data class Reminder(
    val fileInfo: FileInfo,
    val message: String,
    val timestamp: Long,
    val isMuted: Boolean = false,
    val color: ReminderColor,
) {
    fun isExpired() = timestamp <= System.currentTimeMillis()

    fun isActive(): Boolean = !isMuted
}
