package com.github.antonerofeev.intellijplugin.remindme.setting

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import org.jetbrains.annotations.NotNull

@State(
    name = "org.intellij.sdk.settings.AppSettings",
    storages = [Storage("SdkSettingsPlugin.xml")]
)
internal class AppSettings : PersistentStateComponent<AppSettings.State> {
    internal class State {
        var askConfirmBeforeDelete: Boolean = false
    }

    private var settingsState = State()

    override fun getState(): State {
        return settingsState
    }

    override fun loadState(@NotNull state: State) {
        settingsState = state
    }

    companion object {
        val instance: AppSettings
            get() = ApplicationManager.getApplication().getService(AppSettings::class.java)
    }
}