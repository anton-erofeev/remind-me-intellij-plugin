package com.github.antonerofeev.intellijplugin.remindme.ui.notification

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType


internal class ErrorNotification(text: String) : Notification(
    "Reminder Error Notification Group",
    "Remind Me Error",
    "$text <br><b>If this issue persists, please report it at the plugin homepage or GitHub</b>",
    NotificationType.ERROR
)