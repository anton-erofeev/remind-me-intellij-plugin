package com.github.antonerofeev.intellijplugin.remindme.ui.dialog

import com.github.antonerofeev.intellijplugin.remindme.enums.ReminderColor
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.JBColor
import java.awt.Component
import javax.swing.DefaultListCellRenderer
import javax.swing.JList


internal class ColorPicker<T>(
    items: Array<T>,
    initialColorIndex: Int
) : ComboBox<T>(items) {
    init {
        setRenderer(ColorChooserRenderer())
        maximumRowCount = ReminderColor.entries.size
        selectedIndex = initialColorIndex
    }
}

private class ColorChooserRenderer : DefaultListCellRenderer() {
    override fun getListCellRendererComponent(
        list: JList<*>?,
        value: Any,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus)
        val color: JBColor = (value as ReminderColor).color
        background = if (isSelected) {
            color.darker()
        } else {
            color
        }
        return this
    }
}