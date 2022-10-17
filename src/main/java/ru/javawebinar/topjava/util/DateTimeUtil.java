package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static <T extends Comparable<T>> boolean isBetweenHalfOpen(T value, T start, T end) {
        return (start == null || value.compareTo(start) >= 0)  && (end == null || value.compareTo(end) < 0);
    }

    public static LocalDateTime atStartOfDayMin(LocalDate id) {
        return id == null ? LocalDateTime.MIN : id.atStartOfDay();
    }

    public static LocalDateTime atStartOfNextDayMax(LocalDate ld) {
        return ld == null ? LocalDateTime.MAX : ld.plus(1, ChronoUnit.DAYS).atStartOfDay();
    }
    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}

