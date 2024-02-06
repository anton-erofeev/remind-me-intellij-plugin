package com.github.antonerofeev.intellijplugin.remindme.usecase

import com.github.antonerofeev.intellijplugin.remindme.model.Reminder
import com.github.antonerofeev.intellijplugin.remindme.persistent.ReminderStore
import com.github.antonerofeev.intellijplugin.remindme.ui.notification.NotificationScheduler

internal object ScheduleNotification {
    fun execute(id: String, reminder: Reminder) {
        NotificationScheduler.schedule(
            id,
            reminder.message,
            reminder.timestamp,
            { ReminderStore.instance.toggleMutedState(id) },
            { EditReminderDialog.showDialog(id) }
        ) { OpenReminderAnchor.execute(id) }
    }
}