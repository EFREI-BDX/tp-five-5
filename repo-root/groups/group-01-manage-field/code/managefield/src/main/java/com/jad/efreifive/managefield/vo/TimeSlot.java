package com.jad.efreifive.managefield.vo;

import com.jad.efreifive.managefield.exception.InvalidValueObjectException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Set;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimeSlot implements Serializable {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final Set<Long> ALLOWED_DURATIONS_IN_MINUTES = Set.of(60L, 90L, 120L);

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    private TimeSlot(LocalDate date, LocalTime startTime, LocalTime endTime) {
        validate(date, startTime, endTime);
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static TimeSlot of(LocalDate date, LocalTime startTime, LocalTime endTime) {
        return new TimeSlot(date, startTime, endTime);
    }

    public static TimeSlot of(String date, String startTime, String endTime) {
        return new TimeSlot(parseDate(date), parseTime(startTime, "start_time"), parseTime(endTime, "end_time"));
    }

    public boolean overlaps(TimeSlot other) {
        return date.equals(other.date) && startTime.isBefore(other.endTime) && endTime.isAfter(other.startTime);
    }

    public String getDateAsString() {
        return date.toString();
    }

    public String getStartTimeAsString() {
        return startTime.format(TIME_FORMATTER);
    }

    public String getEndTimeAsString() {
        return endTime.format(TIME_FORMATTER);
    }

    private static void validate(LocalDate date, LocalTime startTime, LocalTime endTime) {
        if (date == null) {
            throw new InvalidValueObjectException("date must be a valid ISO-8601 date.");
        }
        if (startTime == null || endTime == null) {
            throw new InvalidValueObjectException("start_time and end_time must be valid times.");
        }
        validateBoundary(startTime, "start_time");
        validateBoundary(endTime, "end_time");
        if (!startTime.isBefore(endTime)) {
            throw new InvalidValueObjectException("start_time must be before end_time on the same date.");
        }
        long duration = Duration.between(startTime, endTime).toMinutes();
        if (!ALLOWED_DURATIONS_IN_MINUTES.contains(duration)) {
            throw new InvalidValueObjectException("reservation duration must be 60, 90, or 120 minutes.");
        }
    }

    private static void validateBoundary(LocalTime time, String fieldName) {
        if (!(time.getMinute() == 0 || time.getMinute() == 30) || time.getSecond() != 0 || time.getNano() != 0) {
            throw new InvalidValueObjectException(fieldName + " must be aligned on a full hour or half hour.");
        }
    }

    private static LocalDate parseDate(String rawDate) {
        if (rawDate == null || rawDate.isBlank()) {
            throw new InvalidValueObjectException("date must not be blank.");
        }
        try {
            return LocalDate.parse(rawDate.trim());
        } catch (DateTimeParseException exception) {
            throw new InvalidValueObjectException("date must use the YYYY-MM-DD format.");
        }
    }

    private static LocalTime parseTime(String rawTime, String fieldName) {
        if (rawTime == null || rawTime.isBlank()) {
            throw new InvalidValueObjectException(fieldName + " must not be blank.");
        }
        try {
            return LocalTime.parse(rawTime.trim(), TIME_FORMATTER);
        } catch (DateTimeParseException exception) {
            throw new InvalidValueObjectException(fieldName + " must use the HH:MM format.");
        }
    }
}
