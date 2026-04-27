package fr.efrei.managefield.domain.valueobject

import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeParseException
import java.util.Objects

/**
 * Value object representing a same-day reservation slot.
 */
@ConsistentCopyVisibility
data class TimeSlot private constructor(
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime
) {
    companion object {
        private val allowedDurations = setOf(60L, 90L, 120L)

        /**
         * Creates a validated time slot from raw HTTP values.
         *
         * @param rawDate date in ISO-8601 `YYYY-MM-DD` format
         * @param rawStartTime start time in `HH:MM` format
         * @param rawEndTime end time in `HH:MM` format
         */
        fun from(rawDate: String, rawStartTime: String, rawEndTime: String): TimeSlot {
            val date = parseDate(Objects.requireNonNull(rawDate, "date must not be null").trim())
            val startTime = parseTime(Objects.requireNonNull(rawStartTime, "start_time must not be null").trim())
            val endTime = parseTime(Objects.requireNonNull(rawEndTime, "end_time must not be null").trim())
            val duration = Duration.between(startTime, endTime).toMinutes()

            require(isHalfHourBoundary(startTime) && isHalfHourBoundary(endTime)) {
                "start_time and end_time must use full-hour or half-hour boundaries"
            }
            require(duration in allowedDurations) {
                "slot duration must be 60, 90, or 120 minutes"
            }

            return TimeSlot(date, startTime, endTime)
        }

        private fun parseDate(value: String): LocalDate {
            require(value.isNotEmpty()) { "date is required" }
            return try {
                LocalDate.parse(value)
            } catch (exception: DateTimeParseException) {
                throw IllegalArgumentException("date must use YYYY-MM-DD format", exception)
            }
        }

        private fun parseTime(value: String): LocalTime {
            require(value.isNotEmpty()) { "time is required" }
            return try {
                LocalTime.parse(value)
            } catch (exception: DateTimeParseException) {
                throw IllegalArgumentException("time must use HH:MM format", exception)
            }
        }

        private fun isHalfHourBoundary(time: LocalTime): Boolean {
            return time.second == 0 && time.nano == 0 && time.minute in setOf(0, 30)
        }
    }

    /**
     * Returns true when this slot overlaps the provided slot.
     */
    fun overlaps(otherStartTime: LocalTime, otherEndTime: LocalTime): Boolean {
        return startTime < otherEndTime && otherStartTime < endTime
    }
}
