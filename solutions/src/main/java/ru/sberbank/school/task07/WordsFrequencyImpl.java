package ru.sberbank.school.task07;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class WordsFrequencyImpl implements WordFrequency<Map<String, Integer>> {
    private FileParser parser;

    public WordsFrequencyImpl(FileParser parser) {
        Objects.requireNonNull(parser, "Парсер не может быть null");
        this.parser = parser;
    }

    @Override
    public Map<String, Integer> countWords(String pathToFile) throws FileNotFoundException {
        List<String> srcStrings = parser.parse(pathToFile);
        Map<String, Integer> resultMap = new HashMap<>();

        for (String string : srcStrings) {
            String[] temp = string.replaceAll("\\p{Punct}", " ")
                    .trim().split("\\s");

            for (String word : temp) {
                if (word.length() > 0) {
                    resultMap.merge(word, 1, (v1, v2) -> v1 + v2);
                }
            }
        }

        return resultMap;
    }
}
