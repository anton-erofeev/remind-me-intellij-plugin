package com.github.antonerofeev.intellijplugin.remindme.model

internal data class Reminder(
        val fileInfo: FileInfo,
        val message: String,
        val timestamp: Long,
        val isMuted: Boolean = false,
) {
    fun isExpired() = timestamp <= System.currentTimeMillis()

    fun isActive(): Boolean = !isMuted
}
