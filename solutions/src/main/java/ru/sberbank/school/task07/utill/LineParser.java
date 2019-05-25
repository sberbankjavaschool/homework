package ru.sberbank.school.task07.utill;

import java.util.Arrays;
import java.util.List;

public class LineParser {
    public static List<String> getWords(String line) {
        return Arrays.asList(line.trim().split("[,;:.!?\\s]+"));
    }
}
