package com.github.antonerofeev.intellijplugin.remindme.persistent

import java.io.Serializable

data class ReminderState(var reminders: MutableMap<String, ReminderDto> = HashMap()) : Serializable