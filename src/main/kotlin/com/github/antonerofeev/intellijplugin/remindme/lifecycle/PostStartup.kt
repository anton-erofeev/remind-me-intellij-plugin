package com.github.antonerofeev.intellijplugin.remindme.lifecycle

import com.github.antonerofeev.intellijplugin.remindme.persistent.ReminderStore
import com.github.antonerofeev.intellijplugin.remindme.usecase.ScheduleNotification
import com.github.antonerofeev.intellijplugin.remindme.usecase.ShowNotification
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity

internal class PostStartup : StartupActivity {

    @Volatile
    private var isScheduled = false
    override fun runActivity(project: Project) {
        val state = ReminderStore.instance.reminders().value

        val activeReminders = state.entries.filter { it.value.isActive() }

        if (!isScheduled) {
            activeReminders.forEach { ScheduleNotification.execute(it.key, it.value) }
            isScheduled = true
        } else {
            activeReminders
                    .filter { it.value.isExpired() }
                    .forEach { ShowNotification.execute(it.key, it.value, project) }
        }
    }
}