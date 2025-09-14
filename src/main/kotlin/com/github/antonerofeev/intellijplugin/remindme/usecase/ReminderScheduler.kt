package com.github.antonerofeev.intellijplugin.remindme.usecase

import com.github.antonerofeev.intellijplugin.remindme.model.FileInfo
import com.github.antonerofeev.intellijplugin.remindme.model.Reminder
import com.github.antonerofeev.intellijplugin.remindme.persistent.ReminderStore
import com.github.antonerofeev.intellijplugin.remindme.ui.notification.ErrorNotification
import com.github.antonerofeev.intellijplugin.remindme.util.extractSelectedText
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys

internal object ReminderScheduler {
    fun scheduleReminderFromActionEvent(
        actionEvent: AnActionEvent,
        timestamp: Long,
        text: String = actionEvent.extractSelectedText()
    ) {
        val file = actionEvent.getData(CommonDataKeys.PSI_FILE)
        val editor = actionEvent.getData(CommonDataKeys.EDITOR)
        if (file?.virtualFile == null || editor == null) {
            ErrorNotification("Can't add reminder. <br> Possible solution: Save project files to disk and retry.")
                .notify(null)
            return
        }

        val reminder = Reminder(
            FileInfo(
                file.virtualFile.name,
                file.virtualFile.presentableUrl,
                file.virtualFile.url,
                editor.caretModel.offset
            ),
            message = text,
            timestamp = timestamp,
        )

        ReminderStore.instance.addReminder(reminder)
    }
}