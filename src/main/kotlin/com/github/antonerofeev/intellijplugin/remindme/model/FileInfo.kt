package com.github.antonerofeev.intellijplugin.remindme.model

internal data class FileInfo(
    val name: String,
    val presentableUri: String,
    val systemUri: String,
    val offset: Int
)
