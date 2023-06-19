package com.github.antonerofeev.intellijplugin.remindme.ui.tool

import com.github.antonerofeev.intellijplugin.remindme.persistent.ReminderStore
import com.github.antonerofeev.intellijplugin.remindme.usecase.OpenReminderAnchor
import com.github.antonerofeev.intellijplugin.remindme.usecase.ShowEditDialog
import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.table.JBTable
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridLayoutManager
import com.intellij.util.ui.JBUI
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTable
import kotlin.math.round
import kotlin.math.roundToInt

@Suppress("JoinDeclarationAndAssignment")
internal class ReminderToolWindow {

    private val windowContent: JPanel = JPanel()

    private val tableModel: ReminderTableModel
    private val reminderTable: JTable

    private val editButton: JButton
    private val deleteButton: JButton
    private val muteButton: JButton

    private val columnWidthPercentage = arrayOf(0.21, 0.29, 0.5)

    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    init {
        tableModel = ReminderTableModel()
        reminderTable = JBTable(tableModel).apply {
            autoCreateRowSorter = true
            addComponentListener(object : ComponentAdapter() {
                override fun componentResized(e: ComponentEvent) {
                    resizeColumns()
                }
            })
        }

        editButton = JButton().setupEdit()
        deleteButton = JButton().setupDelete()
        muteButton = JButton().setupMute()

        windowContent.apply {
            layout = GridLayoutManager(2, 4, JBUI.emptyInsets(), -1, -1, false, false)

            val scrollPane = JScrollPane().apply {
                setViewportView(reminderTable)
            }
            add(scrollPane, GridConstraints(0, 0, 1, 4, 0, 3, 7, 7, null, null, null))
            add(editButton, GridConstraints(1, 0, 1, 1, 0, 1, 3, 0, null, null, null))
            add(muteButton, GridConstraints(1, 2, 1, 1, 0, 1, 3, 0, null, null, null))
            add(deleteButton, GridConstraints(1, 3, 1, 1, 0, 1, 3, 0, null, null, null))
        }

        setControlsEnabled(false)

        scope.launch {
            ReminderStore.instance.reminders().collect { reminders ->
                tableModel.apply {
                    setData(reminders)
                }

                reminderTable.apply {
                    setDefaultRenderer(Any::class.java, ReminderCellRenderer(reminders))
                    selectionModel.addListSelectionListener { _ ->
                        if (this@ReminderToolWindow.allSelectedRows().isNotEmpty()) {
                            val row = this@ReminderToolWindow.currentlySelectedRow()
                            val reminder = tableModel.getReminderAt(row)
                            updateMuteButtonTexts(reminder.isMuted)

                            setControlsEnabled(true)

                            val id = tableModel.getId(row)
                            OpenReminderAnchor.execute(id)
                        }
                    }
                    columnModel.removeColumn(columnModel.getColumn(ReminderTableModel.ID_INDEX))
                    rowSorter.toggleSortOrder(ReminderTableModel.TIME_INDEX)
                }

                resizeColumns()
            }
        }
    }

    private fun resizeColumns() {
        require(columnWidthPercentage.sum() == 1.0)
        require(columnWidthPercentage.size == reminderTable.columnCount)

        val totalWidth = reminderTable.width

        for (index in 0 until reminderTable.columnCount) {
            val column = reminderTable.columnModel.getColumn(index)
            val percentage = columnWidthPercentage[index]

            val width = round(percentage * totalWidth).roundToInt()
            column.preferredWidth = width
        }
    }

    private fun JButton.setupMute(): JButton {
        return apply {
            text = "Mute"
            toolTipText = "Disable selected reminder alarm"
            addActionListener { _ ->
                allSelectedRows().map { tableModel.getId(it) }.forEach { toggleReminderMutedState(it) }
            }
        }
    }

    private fun JButton.setupDelete(): JButton {
        return apply {
            text = "Delete"
            toolTipText = "Delete selected reminder"
            addActionListener { _ ->
                val id = tableModel.getId(currentlySelectedRow())
                removeReminderFromStore(id)
                setControlsEnabled(false)
            }
        }
    }

    private fun JButton.setupEdit(): JButton {
        return apply {
            text = "Edit"
            toolTipText = "Edit selected reminder"
            addActionListener { _ ->
                val id = tableModel.getId(currentlySelectedRow())
                ShowEditDialog.execute(id)
            }
        }
    }

    fun initToolWindow(toolWindow: ToolWindow) {
        val contentFactory: ContentFactory = ContentFactory.getInstance()
        val content: Content = contentFactory.createContent(windowContent, "", false)
        toolWindow.contentManager.addContent(content)
    }

    private fun toggleReminderMutedState(id: String) {
        val reminder = ReminderStore.instance.reminderById(id) ?: return
        updateMuteButtonTexts(!reminder.isMuted)
        ReminderStore.instance.toggleMutedState(id)
        reminderTable.repaint()
    }

    private fun updateMuteButtonTexts(isMuted: Boolean) {
        val (title, tooltip) = if (isMuted) {
            "Unmute" to "Enable selected reminder alarm"
        } else {
            "Mute" to "Disable selected reminder alarm"
        }
        muteButton.apply {
            text = title
            toolTipText = tooltip
        }
    }

    private fun removeReminderFromStore(id: String) {
        ReminderStore.instance.removeReminder(id)
        reminderTable.repaint()
    }

    private fun setControlsEnabled(isEnabled: Boolean) {
        deleteButton.isEnabled = isEnabled
        editButton.isEnabled = isEnabled
        muteButton.isEnabled = isEnabled
    }

    private fun currentlySelectedRow(): Int {
        val row = reminderTable.selectedRow
        if (row == -1) return 0
        return reminderTable.convertRowIndexToModel(row)
    }

    private fun allSelectedRows(): IntArray {
        val rows = reminderTable.selectedRows
        return rows.map { reminderTable.convertRowIndexToModel(it) }.toIntArray()
    }

    fun dispose() {
        scope.cancel()
    }
}

internal class ReminderToolWindowFactory : ToolWindowFactory, Disposable {

    private val toolWindow = ReminderToolWindow()
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        this.toolWindow.initToolWindow(toolWindow)
    }

    override fun dispose() {
        toolWindow.dispose()
    }
}