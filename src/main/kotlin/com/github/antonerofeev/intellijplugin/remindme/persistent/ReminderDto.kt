package com.github.antonerofeev.intellijplugin.remindme.persistent

import java.io.Serializable

data class ReminderDto(
    var name: String? = null,
    var presentableUrl: String? = null,
    var url: String? = null,
    var offset: String? = null,
    var text: String? = null,
    var timestamp: String? = null,
    var done: String? = null,
) : Serializable