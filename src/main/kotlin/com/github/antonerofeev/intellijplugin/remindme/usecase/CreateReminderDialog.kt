package com.github.antonerofeev.intellijplugin.remindme.usecase

import com.github.antonerofeev.intellijplugin.remindme.model.FileInfo
import com.github.antonerofeev.intellijplugin.remindme.model.Reminder
import com.github.antonerofeev.intellijplugin.remindme.persistent.ReminderStore
import com.github.antonerofeev.intellijplugin.remindme.ui.calendar.CalendarDialog
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

internal object CreateReminderDialog {
    fun showDialog() {
        val initMessage = "";
        val calendar = CalendarDialog(initMessage)
        if (calendar.showAndGet()) {
            val dateTime: LocalDateTime = calendar.dateTime
            val systemZone: ZoneId = ZoneId.systemDefault()
            val currentOffsetForMyZone: ZoneOffset = systemZone.rules.getOffset(dateTime)

            val emptyFileInfo = FileInfo("No file name", "", "", 0)
            val reminder = Reminder(
                fileInfo = emptyFileInfo,
                message = calendar.text,
                timestamp = dateTime.toInstant(currentOffsetForMyZone).toEpochMilli(),
            )
            ReminderStore.instance.addReminder(reminder)
        }
    }
}