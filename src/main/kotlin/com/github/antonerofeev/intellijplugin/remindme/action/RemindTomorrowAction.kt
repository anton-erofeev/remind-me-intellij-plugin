package com.github.antonerofeev.intellijplugin.remindme.action

import com.github.antonerofeev.intellijplugin.remindme.usecase.ScheduleReminder
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

internal class RemindTomorrowAction : AnAction() {
    override fun actionPerformed(actionEvent: AnActionEvent) {
        val dateTime = LocalDateTime.now()
                .plusDays(1)
                .withHour(10)
                .withMinute(0)
                .withSecond(0)
        val currentOffset: ZoneOffset = ZoneId.systemDefault().rules.getOffset(dateTime)

        ScheduleReminder.execute(actionEvent, dateTime.toInstant(currentOffset).toEpochMilli())
    }
}