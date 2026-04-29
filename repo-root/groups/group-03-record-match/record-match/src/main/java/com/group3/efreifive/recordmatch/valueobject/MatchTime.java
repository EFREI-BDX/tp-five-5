package com.group3.efreifive.recordmatch.valueobject;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

public record MatchTime(int minute, int second, String period) {

    private static final Set<String> VALID_PERIODS = Set.of("FIRST_HALF", "SECOND_HALF");

    public MatchTime {
        if (minute < 0) {
            throw new IllegalArgumentException("MatchTime.minute must be >= 0, got: " + minute);
        }
        if (second < 0 || second > 59) {
            throw new IllegalArgumentException("MatchTime.second must be between 0 and 59, got: " + second);
        }
        if (period == null || !MatchTime.VALID_PERIODS.contains(period)) {
            throw new IllegalArgumentException("MatchTime.period must be FIRST_HALF or SECOND_HALF, got: " + period);
        }
    }

    public static MatchTime compute(final LocalDateTime startedAt,
                                    final int scheduledDurationMinutes,
                                    final LocalDateTime occuredAt) {
        final long totalSeconds = Duration.between(startedAt, occuredAt).getSeconds();
        final long halfDurationSeconds = (scheduledDurationMinutes / 2L) * 60L;

        final String period;
        final long elapsedInPeriod;
        if (totalSeconds <= halfDurationSeconds) {
            period = "FIRST_HALF";
            elapsedInPeriod = totalSeconds;
        } else {
            period = "SECOND_HALF";
            elapsedInPeriod = totalSeconds - halfDurationSeconds;
        }

        return new MatchTime((int) (elapsedInPeriod / 60), (int) (elapsedInPeriod % 60), period);
    }
}
