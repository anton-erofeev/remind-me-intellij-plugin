package com.github.antonerofeev.intellijplugin.remindme.usecase

import com.github.antonerofeev.intellijplugin.remindme.persistent.ReminderStore
import com.github.antonerofeev.intellijplugin.remindme.ui.calendar.ReminderDialog
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

internal object EditReminderDialog {
    fun showDialog(reminderId: String) {
        val reminder = ReminderStore.instance.reminderById(reminderId) ?: return

        val reminderDialog = ReminderDialog(reminder.message, reminder.timestamp, reminder.color)
        if (reminderDialog.showAndGet()) {
            val dateTime: LocalDateTime = reminderDialog.dateTime
            val systemZone: ZoneId = ZoneId.systemDefault()
            val currentOffsetForMyZone: ZoneOffset = systemZone.rules.getOffset(dateTime)

            ReminderStore.instance.editReminder(
                reminderId,
                dateTime.toInstant(currentOffsetForMyZone).toEpochMilli(),
                reminderDialog.text,
                reminderDialog.color
            )
        }
    }
}