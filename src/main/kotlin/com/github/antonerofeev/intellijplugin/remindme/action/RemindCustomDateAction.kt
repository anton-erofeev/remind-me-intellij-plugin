package com.github.antonerofeev.intellijplugin.remindme.action

import com.github.antonerofeev.intellijplugin.remindme.ui.calendar.CalendarDialog
import com.github.antonerofeev.intellijplugin.remindme.usecase.ReminderScheduler
import com.github.antonerofeev.intellijplugin.remindme.util.extractSelectedText
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

internal class RemindCustomDateAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val calendar = CalendarDialog(event.extractSelectedText())
        if (calendar.showAndGet()) {
            val dateTime: LocalDateTime = calendar.dateTime
            val zoneOffset: ZoneOffset = ZoneId.systemDefault().rules.getOffset(dateTime)
            val timestamp: Long = dateTime.toInstant(zoneOffset).toEpochMilli();

            ReminderScheduler.scheduleReminderFromActionEvent(event, timestamp, calendar.text)
        }
    }

}