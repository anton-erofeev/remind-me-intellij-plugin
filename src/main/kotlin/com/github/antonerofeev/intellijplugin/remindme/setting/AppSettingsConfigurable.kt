package com.github.antonerofeev.intellijplugin.remindme.setting

import com.intellij.openapi.options.Configurable
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.Nullable
import java.util.*
import javax.swing.JComponent


/**
 * Provides controller functionality for application settings.
 */
internal class AppSettingsConfigurable : Configurable {
    private var appSettingsComponent: AppSettingsComponent? = null

    override fun getPreferredFocusedComponent(): JComponent {
        return appSettingsComponent!!.preferredFocusedComponent()
    }

    @Nullable
    override fun createComponent(): JComponent {
        appSettingsComponent = AppSettingsComponent()
        return appSettingsComponent!!.panel
    }

    override fun isModified(): Boolean {
        val state: AppSettings.State = Objects.requireNonNull(AppSettings.instance.state)
        return appSettingsComponent!!.askBeforeDeleteReminderSetting != state.askConfirmBeforeDelete
    }

    override fun apply() {
        val state: AppSettings.State = Objects.requireNonNull(AppSettings.instance.state)
        state.askConfirmBeforeDelete = appSettingsComponent!!.askBeforeDeleteReminderSetting
    }

    override fun reset() {
        val state: AppSettings.State = Objects.requireNonNull(AppSettings.instance.state)
        appSettingsComponent!!.askBeforeDeleteReminderSetting = state.askConfirmBeforeDelete
    }

    override fun disposeUIResources() {
        appSettingsComponent = null
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    override fun getDisplayName(): String {
        return "REMIND ME LATER SETTINGS"
    }

}