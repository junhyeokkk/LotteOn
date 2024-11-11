package com.team1.lotteon.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/*
    날짜 : 2024/10/25
    이름 : 최준혁
    내용 : 날짜 포맷팅 Util
*/
@Component
public class DateUtil {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public String formatLocalDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(formatter) : "";
    }
        // "yyyy-MM-dd" 형식으로 날짜만 포맷하는 메서드
        private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        public String formatDate(LocalDateTime dateTime) {
            return dateTime != null ? dateTime.format(dateFormatter) : "";
        }
    }

