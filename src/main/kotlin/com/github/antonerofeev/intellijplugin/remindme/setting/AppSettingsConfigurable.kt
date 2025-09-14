package com.github.antonerofeev.intellijplugin.remindme.setting

import com.intellij.openapi.options.Configurable
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.Nullable
import java.util.*
import javax.swing.JComponent


/**
 * Provides controller functionality for application settings UI in the plugin.
 * Handles creation, modification, and disposal of the settings panel.
 */
internal class AppSettingsConfigurable : Configurable {
    private var appSettingsComponent: AppSettingsComponent? = null

    /**
     * Returns the component that should be focused when the settings panel is opened.
     */
    override fun getPreferredFocusedComponent(): JComponent? =
        appSettingsComponent?.preferredFocusedComponent()

    /**
     * Creates and returns the main settings UI component.
     */
    @Nullable
    override fun createComponent(): JComponent {
        appSettingsComponent = AppSettingsComponent()
        return appSettingsComponent!!.panel
    }

    /**
     * Checks if the settings have been modified by the user.
     */
    override fun isModified(): Boolean {
        val state = AppSettings.instance.state
        val current = appSettingsComponent?.askBeforeDeleteReminderSetting
        return current != null && current != state.askConfirmBeforeDelete
    }

    /**
     * Applies the changes made in the settings UI to the persistent state.
     */
    override fun apply() {
        val state = AppSettings.instance.state
        appSettingsComponent?.let {
            state.askConfirmBeforeDelete = it.askBeforeDeleteReminderSetting
        }
    }

    /**
     * Resets the settings UI to the current persistent state.
     */
    override fun reset() {
        val state = AppSettings.instance.state
        appSettingsComponent?.askBeforeDeleteReminderSetting = state.askConfirmBeforeDelete
    }

    /**
     * Disposes of the UI resources when the settings panel is closed.
     */
    override fun disposeUIResources() {
        appSettingsComponent = null
    }

    /**
     * Returns the display name for the settings panel.
     */
    @Nls(capitalization = Nls.Capitalization.Title)
    override fun getDisplayName(): String = "REMIND ME LATER SETTINGS"
}