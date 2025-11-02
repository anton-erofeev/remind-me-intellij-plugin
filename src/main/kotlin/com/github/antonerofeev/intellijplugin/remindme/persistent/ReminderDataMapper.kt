package com.github.antonerofeev.intellijplugin.remindme.persistent

import com.github.antonerofeev.intellijplugin.remindme.enums.ReminderColor
import com.github.antonerofeev.intellijplugin.remindme.model.FileInfo
import com.github.antonerofeev.intellijplugin.remindme.model.Reminder
import java.io.File

internal class ReminderDataMapper {

    fun map(dto: ReminderDto): Reminder {
        val url = dto.url ?: ""
        val color: ReminderColor = ReminderColor.valueOf(dto.color ?: ReminderColor.DEFAULT.name)
        return Reminder(
            FileInfo(
                dto.name ?: url.split("/").last(),
                dto.presentableUrl ?: url.replace("file://", "").replace('/', File.separatorChar),
                dto.url ?: "",
                dto.offset?.toInt() ?: 0,
            ),
            dto.text ?: "",
            dto.timestamp?.toLong() ?: 0L,
            dto.done?.toBooleanStrictOrNull() ?: false,
            color,
        )
    }

    fun map(reminder: Reminder): ReminderDto {
        return ReminderDto(
            reminder.fileInfo.name,
            reminder.fileInfo.presentableUri,
            reminder.fileInfo.systemUri,
            reminder.fileInfo.offset.toString(),
            reminder.message,
            reminder.timestamp.toString(),
            reminder.isMuted.toString(),
            reminder.color.toString()
        )
    }
}