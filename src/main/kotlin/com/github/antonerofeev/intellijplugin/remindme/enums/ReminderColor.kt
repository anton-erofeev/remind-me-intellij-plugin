package com.github.antonerofeev.intellijplugin.remindme.enums

import com.intellij.ui.JBColor

enum class ReminderColor(val color: JBColor) {
    DEFAULT(JBColor.LIGHT_GRAY),
    BLUE(JBColor.BLUE),
    CYAN(JBColor.CYAN as JBColor),
    GREEN(JBColor.GREEN),
    MAGENTA(JBColor.MAGENTA as JBColor),
    ORANGE(JBColor.ORANGE),
    PINK(JBColor.PINK),
    YELLOW(JBColor.YELLOW),
}