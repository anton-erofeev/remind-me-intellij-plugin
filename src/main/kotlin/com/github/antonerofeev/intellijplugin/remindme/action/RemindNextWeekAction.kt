package com.github.antonerofeev.intellijplugin.remindme.action

import com.github.antonerofeev.intellijplugin.remindme.usecase.ScheduleReminder
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.temporal.TemporalAdjusters

internal class RemindNextWeekAction : AnAction() {
    override fun actionPerformed(actionEvent: AnActionEvent) {

        val dateTime = LocalDateTime.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .withHour(10)
                .withMinute(0)
                .withSecond(0)

        val currentOffset: ZoneOffset = ZoneId.systemDefault().rules.getOffset(dateTime)

        ScheduleReminder.execute(actionEvent, dateTime.toInstant(currentOffset).toEpochMilli())
    }
}