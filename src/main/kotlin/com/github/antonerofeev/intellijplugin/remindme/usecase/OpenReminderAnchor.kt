package com.github.antonerofeev.intellijplugin.remindme.usecase

import com.github.antonerofeev.intellijplugin.remindme.persistent.ReminderStore
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.wm.WindowManager
import java.awt.Window


internal object OpenReminderAnchor {
    fun execute(id: String) {
        val reminder = ReminderStore.instance.reminderById(id) ?: return
        val project = getCurrentProject() ?: return
        val file = VirtualFileManager.getInstance().findFileByUrl(reminder.fileInfo.systemUri) ?: return

        try {
            val openFileDescriptor = OpenFileDescriptor(project, file, reminder.fileInfo.offset)
            FileEditorManager.getInstance(project).openTextEditor(openFileDescriptor, true)
        } catch (ignore: IllegalArgumentException) {
        }
    }

    private fun getCurrentProject(): Project? {
        ProjectManager.getInstance().openProjects.forEach { project ->
            val window: Window? = WindowManager.getInstance().suggestParentWindow(project)
            if (window?.isActive == true) return project
        }
        return null
    }
}