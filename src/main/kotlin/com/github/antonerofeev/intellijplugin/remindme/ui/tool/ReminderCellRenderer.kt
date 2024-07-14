package com.github.antonerofeev.intellijplugin.remindme.ui.tool

import com.github.antonerofeev.intellijplugin.remindme.model.Reminder
import com.github.antonerofeev.intellijplugin.remindme.util.formatTimestamp
import com.intellij.ui.JBColor
import java.awt.Component
import java.time.format.FormatStyle
import javax.swing.JTable
import javax.swing.border.LineBorder
import javax.swing.table.DefaultTableCellRenderer
import javax.swing.table.DefaultTableModel


internal class ReminderCellRenderer(private val reminders: Map<String, Reminder>) : DefaultTableCellRenderer() {

    override fun getTableCellRendererComponent(
        table: JTable,
        any: Any,
        isSelected: Boolean,
        hasFocus: Boolean,
        rowIndex: Int,
        colIndex: Int
    ): Component {
        val row = table.convertRowIndexToModel(rowIndex)
        val tableModel = table.model as ReminderTableModel

        var value = any
        if (tableModel.isTimestampColumn(colIndex)) {
            value = (value as Long).formatTimestamp(FormatStyle.LONG)
        }

        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, colIndex)

        val id: String = tableModel.getId(row)
        val reminder = reminders[id] ?: return this

        foreground = when {
            reminder.isMuted -> JBColor.GRAY
            tableModel.isExpired(row) -> JBColor.RED
            else -> table.foreground
        }

        if (isSelected) {
            border = LineBorder(table.selectionBackground, 2, false)
        }

        toolTipText = if (colIndex == ReminderTableModel.FILE_NAME_INDEX) {
            reminder.fileInfo.presentableUri
        } else {
            value.toString()
        }

        return this
    }
}

internal class ReminderTableModel : DefaultTableModel(columnNames, 0) {
    companion object {
        private val columnNames = arrayOf("File Name", "Time", "Message", "Id")
        const val FILE_NAME_INDEX = 0
        const val TIME_INDEX = 1
        const val MESSAGE_INDEX = 2
        const val ID_INDEX = 3
    }

    private val reminders = mutableMapOf<String, Reminder>()

    override fun isCellEditable(i: Int, i1: Int): Boolean {
        return false
    }

    fun setData(data: Map<String, Reminder>) {
        reminders.clear()
        reminders.putAll(data)

        val content = data.map { entry ->
            val id = entry.key
            val reminder = entry.value
            arrayOf<Any>(
                reminder.fileInfo.name,
                reminder.timestamp,
                reminder.message,
                id
            )
        }.toTypedArray()

        setDataVector(convertToVector(content), convertToVector(columnNames))
    }

    fun getId(row: Int): String {
        return getValueAt(row, ID_INDEX) as String
    }

    fun getReminderAt(row: Int): Reminder {
        val id = getId(row)
        return reminders[id] ?: throw IllegalStateException("Reminder not found")
    }

    fun isExpired(row: Int): Boolean {
        return System.currentTimeMillis() > getValueAt(row, TIME_INDEX) as Long
    }

    fun isTimestampColumn(column: Int): Boolean {
        return column == TIME_INDEX
    }

}
