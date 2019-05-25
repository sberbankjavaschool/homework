package ru.sberbank.school.task07;

import lombok.NonNull;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class WordsFrequencyImpl implements WordFrequency<Map<String, Integer>> {

    private FileParser parser;

    public WordsFrequencyImpl(@NonNull FileParser parser) {
        this.parser = parser;
    }

    @Override
    public Map<String, Integer> countWords(String pathToFile) throws FileNotFoundException {

        Map<String, Integer> map = new HashMap<>();

        parser.parse(pathToFile).
            forEach(s -> SentenceParser.parse(s).
                forEach(w -> map.merge(w, 1, Integer::sum)));

        return map;
    }
}
