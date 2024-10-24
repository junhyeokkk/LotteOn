package com.team1.lotteon.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateUtil {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public String formatLocalDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(formatter) : "";
    }
}