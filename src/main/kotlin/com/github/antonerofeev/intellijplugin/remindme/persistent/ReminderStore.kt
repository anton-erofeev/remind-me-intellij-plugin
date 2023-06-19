package com.github.antonerofeev.intellijplugin.remindme.persistent

import com.github.antonerofeev.intellijplugin.remindme.model.Reminder
import com.github.antonerofeev.intellijplugin.remindme.usecase.ScheduleNotification
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import kotlinx.coroutines.flow.*
import java.util.*

internal interface ReminderStore {
    fun reminders(): StateFlow<Map<String, Reminder>>

    fun reminderById(id: String): Reminder?

    fun observeReminder(id: String): Flow<Reminder>

    fun addReminder(reminder: Reminder, id: String = UUID.randomUUID().toString())

    fun editReminder(id: String, timestamp: Long, text: String)

    fun toggleMutedState(id: String)

    fun removeReminder(id: String)

    companion object {
        val instance: ReminderStore
            get() = ApplicationManager.getApplication().getService(ReminderPersistentStore::class.java)
    }
}

@State(name = "Reminders", storages = [Storage(value = "remindme.xml")])
internal class ReminderPersistentStore : PersistentStateComponent<ReminderState>, ReminderStore {

    private val state = MutableStateFlow(emptyMap<String, Reminder>())

    private val mapper = ReminderDataMapper()

    override fun reminders() = state
    override fun reminderById(id: String): Reminder? = state.value[id]

    override fun observeReminder(id: String): Flow<Reminder> =
        state.filter { map -> map[id] != null }
            .map { map -> map[id]!! }

    override fun addReminder(reminder: Reminder, id: String) {
        state.update { prev ->
            val mutable = prev.toMutableMap()
            mutable[id] = reminder
            return@update mutable
        }

        ScheduleNotification.execute(id, reminder)
    }

    override fun editReminder(id: String, timestamp: Long, text: String) {
        val reminder = state.value[id] ?: return

        val updated = reminder.copy(
            isMuted = false,
            timestamp = timestamp,
            message = text
        )

        addReminder(updated, id)
    }

    override fun toggleMutedState(id: String) {
        val reminder = state.value[id] ?: return

        val updated = reminder.copy(
            isMuted = !reminder.isMuted
        )

        addReminder(updated, id)
    }

    override fun removeReminder(id: String) {
        state.update { prev -> prev.minus(id) }
    }

    override fun getState(): ReminderState {
        return ReminderState(
            state.value.mapValues { mapper.map(it.value) }
                .toMutableMap()
        )
    }

    override fun loadState(state: ReminderState) {
        this.state.update {
            state.reminders.mapValues { mapper.map(it.value) }
        }
    }
}