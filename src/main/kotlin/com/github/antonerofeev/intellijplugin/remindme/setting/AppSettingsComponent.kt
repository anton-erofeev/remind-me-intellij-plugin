package com.github.antonerofeev.intellijplugin.remindme.setting

import com.intellij.ui.components.JBCheckBox
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

class AppSettingsComponent {
    val panel: JPanel
    private val askBeforeDeleteCheckBox = JBCheckBox("Ask for confirmation before deleting a reminder")

    init {
        panel = FormBuilder.createFormBuilder()
            .addComponent(askBeforeDeleteCheckBox, 1)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    fun preferredFocusedComponent(): JComponent {
        return askBeforeDeleteCheckBox
    }

    var askBeforeDeleteReminderSetting: Boolean
        get() = askBeforeDeleteCheckBox.isSelected
        set(newStatus) {
            askBeforeDeleteCheckBox.isSelected = newStatus
        }
}