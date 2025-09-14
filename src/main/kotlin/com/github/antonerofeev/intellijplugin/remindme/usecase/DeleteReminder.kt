package com.github.antonerofeev.intellijplugin.remindme.usecase

import com.github.antonerofeev.intellijplugin.remindme.persistent.ReminderStore
import com.github.antonerofeev.intellijplugin.remindme.setting.AppSettings
import com.github.antonerofeev.intellijplugin.remindme.ui.dialog.DeleteReminderDialog

internal object DeleteReminder {
    fun execute(reminderId: String): Boolean {
        if (AppSettings.instance.state.askConfirmBeforeDelete) {
            val dialog = DeleteReminderDialog()
            if (dialog.showAndGet()) {
                ReminderStore.instance.removeReminder(reminderId)
            } else {
                return false
            }
            if (dialog.doNotShowAgain) {
                AppSettings.instance.state.askConfirmBeforeDelete = false
            }
        } else {
            ReminderStore.instance.removeReminder(reminderId)
        }
        return true
    }
}