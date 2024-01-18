package com.example.tripster.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd. MMM yyyy.");

    public static long getCurrentDateInMillis() {
        LocalDateTime today = LocalDate.now().atStartOfDay();
        return today.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public static LocalDate toLocalDate(long millis) {
        return Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static long toMillis(LocalDate date) {
        return date.atStartOfDay()
                .toInstant(ZoneOffset.UTC)
                .toEpochMilli();
    }

    public static String toString(LocalDate date) {
        return date.format(formatter);
    }

    public static LocalDate toLocalDate(String date) {
        return LocalDate.parse(date, formatter);
    }

}
