package com.github.antonerofeev.intellijplugin.remindme.ui.dialog

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.util.ui.JBUI
import java.awt.BorderLayout
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

class DeleteReminderDialog : DialogWrapper(true) {
    private lateinit var checkBox: JCheckBox
    init {
        title = "Delete Reminder"
        init()
    }

    override fun createCenterPanel(): JComponent {
        val panel = JPanel(BorderLayout())
        panel.border = JBUI.Borders.empty(20)
        val label = JLabel("Are you sure you want to delete the reminder?")
        panel.add(label, BorderLayout.CENTER)

        checkBox = JCheckBox("Don't ask again")
        panel.add(checkBox, BorderLayout.SOUTH)
        return panel
    }

    fun isDoNotShowAgainChecked(): Boolean {
        return checkBox.isSelected
    }
}