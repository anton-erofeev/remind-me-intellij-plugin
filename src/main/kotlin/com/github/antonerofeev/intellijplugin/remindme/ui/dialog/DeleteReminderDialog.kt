package com.github.antonerofeev.intellijplugin.remindme.ui.dialog

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.util.ui.JBUI
import java.awt.BorderLayout
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

/**
 * Dialog for confirming reminder deletion with an optional "Don't ask again" checkbox.
 */
internal class DeleteReminderDialog : DialogWrapper(true) {

    /**
     * Checkbox for "Don't ask again" option.
     */
    private val checkBox = JCheckBox("Don't ask again")

    init {
        title = "Delete Reminder"
        init()
    }

    /**
     * Creates the main content panel for the dialog.
     */
    override fun createCenterPanel(): JComponent = JPanel(BorderLayout()).apply {
        border = JBUI.Borders.empty(20)
        add(JLabel("Are you sure you want to delete the reminder?"), BorderLayout.CENTER)
        add(checkBox, BorderLayout.SOUTH)
    }

    /**
     * Returns true if the user checked "Don't ask again".
     */
    val doNotShowAgain: Boolean
        get() = checkBox.isSelected
}