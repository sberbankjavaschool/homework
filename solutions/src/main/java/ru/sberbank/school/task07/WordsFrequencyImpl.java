package ru.sberbank.school.task07;

import lombok.NonNull;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordsFrequencyImpl implements WordFrequency<Map<String, Integer>> {

    private FileParser parser;

    public WordsFrequencyImpl(@NonNull FileParser parser) {
        this.parser = parser;
    }

    @Override
    public Map<String, Integer> countWords(String pathToFile) throws FileNotFoundException {

        List<String> list = parser.parse(pathToFile);
        Map<String, Integer> map = new HashMap<>();

        for (String s : list) {
            String[] words = s.trim().split("\\s+");

            for (String word : words) {
                Integer count = map.getOrDefault(word.toLowerCase(), 0);
                map.put(word, ++count);
            }

        }

        return map;
    }

}
