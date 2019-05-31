package ru.sberbank.school.task07;

import java.util.Arrays;
import java.util.List;

public class SentenceParser {
    public static List<String> parse(String sentence) {
        return Arrays.asList(sentence.split("\\W+"));
    }
}
