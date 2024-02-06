package com.github.antonerofeev.intellijplugin.remindme.usecase

import com.github.antonerofeev.intellijplugin.remindme.persistent.ReminderStore
import com.github.antonerofeev.intellijplugin.remindme.ui.calendar.CalendarDialog
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

internal object EditReminderDialog {
    fun showDialog(reminderId: String) {
        val reminder = ReminderStore.instance.reminderById(reminderId) ?: return

        val calendar = CalendarDialog(reminder.message, reminder.timestamp)
        if (calendar.showAndGet()) {
            val dateTime: LocalDateTime = calendar.dateTime
            val systemZone: ZoneId = ZoneId.systemDefault()
            val currentOffsetForMyZone: ZoneOffset = systemZone.rules.getOffset(dateTime)

            ReminderStore.instance.editReminder(
                reminderId,
                dateTime.toInstant(currentOffsetForMyZone).toEpochMilli(),
                calendar.text
            )
        }
    }
}