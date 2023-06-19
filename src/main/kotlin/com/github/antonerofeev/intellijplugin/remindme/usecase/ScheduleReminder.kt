package com.github.antonerofeev.intellijplugin.remindme.usecase

import com.github.antonerofeev.intellijplugin.remindme.model.FileInfo
import com.github.antonerofeev.intellijplugin.remindme.model.Reminder
import com.github.antonerofeev.intellijplugin.remindme.persistent.ReminderStore
import com.github.antonerofeev.intellijplugin.remindme.ui.notification.ErrorNotification
import com.github.antonerofeev.intellijplugin.remindme.util.extractSelectedText
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile

internal object ScheduleReminder {
    fun execute(actionEvent: AnActionEvent, timestamp: Long, text: String = actionEvent.extractSelectedText()) {
        val file: PsiFile? = actionEvent.getData(CommonDataKeys.PSI_FILE)
        if (file?.virtualFile == null) {
            ErrorNotification("Can't add reminder. <br> Possible solution: Save project files to disk and retry.")
                .notify(null)
            return
        }

        val editor: Editor = actionEvent.getRequiredData(CommonDataKeys.EDITOR)

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