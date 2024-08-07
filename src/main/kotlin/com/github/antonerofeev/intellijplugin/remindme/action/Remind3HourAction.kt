package com.github.antonerofeev.intellijplugin.remindme.action

import com.github.antonerofeev.intellijplugin.remindme.usecase.ReminderScheduler
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import java.util.concurrent.TimeUnit

internal class Remind3HourAction : AnAction() {
    override fun actionPerformed(actionEvent: AnActionEvent) {
        ReminderScheduler.scheduleReminderFromActionEvent(
            actionEvent, System.currentTimeMillis() + TimeUnit.HOURS.toMillis(3))
    }
}