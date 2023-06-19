package com.github.antonerofeev.intellijplugin.remindme.util

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys

internal fun AnActionEvent.extractSelectedText(): String {
    val editor = getRequiredData(CommonDataKeys.EDITOR)
    val caretModel = editor.caretModel
    val selectedText = caretModel.currentCaret.selectedText

    if (selectedText?.isNotBlank() == true) {
        return selectedText
    }

    val lineStart = caretModel.visualLineStart
    val lineEnd = caretModel.visualLineEnd
    val text = editor.document.charsSequence

    return text.subSequence(lineStart, lineEnd).toString().trim()
}