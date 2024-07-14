package com.github.antonerofeev.intellijplugin.remindme.lifecycle

import com.github.antonerofeev.intellijplugin.remindme.model.Reminder
import com.github.antonerofeev.intellijplugin.remindme.persistent.ReminderStore
import com.github.antonerofeev.intellijplugin.remindme.usecase.ScheduleNotification
import com.github.antonerofeev.intellijplugin.remindme.usecase.ShowNotification
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import java.util.concurrent.atomic.AtomicBoolean

internal class PostStartup : StartupActivity {

    private val isScheduled = AtomicBoolean(false)

    override fun runActivity(project: Project) {
        val state = ReminderStore.instance.reminders().value
        val activeReminders = state.entries.filter { it.value.isActive() }

        if (isScheduled.compareAndSet(false, true)) {
            scheduleNotifications(activeReminders)
        } else {
            showExpiredNotifications(activeReminders, project)
        }
    }

    private fun scheduleNotifications(activeReminders: List<Map.Entry<String, Reminder>>) {
        activeReminders.forEach { ScheduleNotification.execute(it.key, it.value) }
    }

    private fun showExpiredNotifications(activeReminders: List<Map.Entry<String, Reminder>>, project: Project) {
        activeReminders
            .filter { it.value.isExpired() }
            .forEach { ShowNotification.execute(it.key, it.value, project) }
    }
}