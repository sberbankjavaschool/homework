package ru.sberbank.school.task02.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CurrentDate {

    private static final DateTimeFormatter timeFormatter =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public static String getDate() {
        LocalDateTime date = LocalDateTime.now();
        return date.format(timeFormatter);
    }

}
