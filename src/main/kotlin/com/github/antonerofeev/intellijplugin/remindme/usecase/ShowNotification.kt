package com.github.antonerofeev.intellijplugin.remindme.usecase

import com.github.antonerofeev.intellijplugin.remindme.model.Reminder
import com.github.antonerofeev.intellijplugin.remindme.persistent.ReminderStore
import com.github.antonerofeev.intellijplugin.remindme.ui.notification.NotificationScheduler
import com.intellij.openapi.project.Project

internal object ShowNotification {
    fun execute(id: String, reminder: Reminder, project: Project) {
        NotificationScheduler.notify(
            reminder.message,
            reminder.timestamp,
            { ReminderStore.instance.toggleMutedState(id) },
            { EditReminderDialog.showDialog(id) },
            { OpenReminderAnchor.execute(id) },
            project,
        )
    }
}