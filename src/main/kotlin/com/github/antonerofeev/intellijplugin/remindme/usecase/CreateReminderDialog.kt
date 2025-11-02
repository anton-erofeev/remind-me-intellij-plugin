package com.github.antonerofeev.intellijplugin.remindme.usecase

import com.github.antonerofeev.intellijplugin.remindme.model.FileInfo
import com.github.antonerofeev.intellijplugin.remindme.model.Reminder
import com.github.antonerofeev.intellijplugin.remindme.persistent.ReminderStore
import com.github.antonerofeev.intellijplugin.remindme.ui.calendar.ReminderDialog
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

internal object CreateReminderDialog {
    fun showDialog() {
        val initMessage = "";
        val reminderDialog = ReminderDialog(initMessage)
        if (reminderDialog.showAndGet()) {
            val dateTime: LocalDateTime = reminderDialog.dateTime
            val systemZone: ZoneId = ZoneId.systemDefault()
            val currentOffsetForMyZone: ZoneOffset = systemZone.rules.getOffset(dateTime)

            val emptyFileInfo = FileInfo("No file name", "", "", 0)
            val reminder = Reminder(
                fileInfo = emptyFileInfo,
                message = reminderDialog.text,
                timestamp = dateTime.toInstant(currentOffsetForMyZone).toEpochMilli(),
                color = reminderDialog.color,
            )
            ReminderStore.instance.addReminder(reminder)
        }
    }
}