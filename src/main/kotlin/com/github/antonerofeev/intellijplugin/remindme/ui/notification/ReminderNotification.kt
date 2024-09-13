package com.github.antonerofeev.intellijplugin.remindme.ui.notification

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

internal class ReminderNotification(
    text: String,
    alarmTime: String,
    onMute: () -> Unit,
    onReschedule: () -> Unit,
    onLocate: () -> Unit,
) : Notification(
    "Reminder Notification Group",
    text,
    NotificationType.INFORMATION
) {
    init {
        setTitle("Remind me", alarmTime)
        addAction(object : AnAction("Locate") {
            override fun actionPerformed(e: AnActionEvent) {
                onLocate()
            }

        })
        addAction(object : AnAction("Reschedule") {
            override fun actionPerformed(e: AnActionEvent) {
                onReschedule()
            }

        })
        addAction(object : AnAction("Mute") {
            override fun actionPerformed(e: AnActionEvent) {
                onMute()
                hideBalloon()
            }
        })

    }
}