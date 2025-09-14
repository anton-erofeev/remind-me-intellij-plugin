package com.github.antonerofeev.intellijplugin.remindme.ui.calendar

import com.github.lgooddatepicker.components.DatePickerSettings
import com.github.lgooddatepicker.components.DateTimePicker
import com.github.lgooddatepicker.components.TimePickerSettings
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.JBColor
import java.awt.BorderLayout
import java.awt.Dimension
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.swing.*

/**
 * Dialog for selecting a date, time, and entering reminder text.
 * Provides a date-time picker and a text area for user input.
 */
internal class CalendarDialog(
    reminderText: String,
    timestamp: Long = Instant.now().toEpochMilli()
) : DialogWrapper(true) {

    /**
     * Date and time picker component.
     */
    private val dateTimePicker: DateTimePicker = DateTimePicker()

    /**
     * Text area for entering reminder message.
     */
    private val textArea: JTextArea = JTextArea(reminderText)

    /**
     * Returns the selected date and time, or throws if not set.
     */
    val dateTime: LocalDateTime
        get() = dateTimePicker.dateTimeStrict
            ?: throw IllegalStateException("Date and time must be selected.")

    /**
     * Returns the entered reminder text.
     */
    val text: String
        get() = textArea.text

    init {
        dateTimePicker.dateTimeStrict = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
        title = "Create Reminder"
        init()
    }

    /**
     * Creates the main content panel for the dialog.
     */
    override fun createCenterPanel(): JComponent = JPanel(BorderLayout(10, 10)).apply {
        preferredSize = Dimension(520, 100)
        textArea.configureTextArea()
        dateTimePicker.configureTimePicker()
        add(textArea, BorderLayout.NORTH)
        add(dateTimePicker, BorderLayout.SOUTH)
    }

    /**
     * Configures the date and time picker appearance and behavior.
     */
    private fun DateTimePicker.configureTimePicker() {
        getDatePicker().settings.apply {
            allowEmptyDates = false
            setColor(DatePickerSettings.DateArea.TextFieldBackgroundValidDate, JBColor.BLACK.darker())
            setColor(DatePickerSettings.DateArea.BackgroundOverallCalendarPanel, JBColor.BLACK.darker())
            setColor(DatePickerSettings.DateArea.BackgroundTodayLabel, JBColor.BLACK.darker())
            setColor(DatePickerSettings.DateArea.TextTodayLabel, JBColor.LIGHT_GRAY)
            setColor(DatePickerSettings.DateArea.BackgroundMonthAndYearMenuLabels, JBColor.BLACK.darker())
            setColor(DatePickerSettings.DateArea.TextMonthAndYearMenuLabels, JBColor.LIGHT_GRAY)
            setColor(DatePickerSettings.DateArea.CalendarBackgroundNormalDates, JBColor.BLACK)
            setColor(DatePickerSettings.DateArea.CalendarTextNormalDates, JBColor.LIGHT_GRAY)
        }
        getTimePicker().apply {
            settings.apply {
                allowEmptyTimes = false
                displaySpinnerButtons = true
                setColor(TimePickerSettings.TimeArea.TextFieldBackgroundValidTime, JBColor.BLACK.darker())
            }
            setTimeToNow()
        }
    }

    /**
     * Configures the text area appearance.
     */
    private fun JTextArea.configureTextArea() {
        maximumSize = Dimension(400, 30)
        border = BorderFactory.createCompoundBorder(
            UIManager.getBorder("TextField.border"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        )
    }
}