package ru.sberbank.school.task07;

import lombok.NonNull;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordFrequencyImpl implements WordFrequency<Map<String, Integer>> {

    private final FileParser parser;

    public WordFrequencyImpl(@NonNull FileParser parser) {
        this.parser = parser;
    }

    @Override
    public Map<String, Integer> countWords(String pathToFile) throws FileNotFoundException {
        Map<String, Integer> result = new HashMap<>();
        List<String> fileStrings = parser.parse(pathToFile);
        String[] splitted;
        for (String line : fileStrings) {
            splitted = line.split(" ");
            for (String s : splitted) {
                s = s.toLowerCase();
                if (result.containsKey(s)) {
                    result.put(s, result.get(s) + 1);
                } else {
                    result.put(s, 1);
                }
            }
        }
        return result;
    }
}
