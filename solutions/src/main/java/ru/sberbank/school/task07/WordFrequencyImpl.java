package ru.sberbank.school.task07;

import java.io.FileNotFoundException;
import java.util.*;

public class WordFrequencyImpl implements WordFrequency<Map<String, Integer>> {
    private FileParser parser;

    public WordFrequencyImpl(FileParser parser) {
        this.parser = parser;
    }

    @Override
    public Map<String, Integer> countWords(String pathToFile) throws FileNotFoundException {
        List<String> list = parser.parse(pathToFile);

        Map<String, Integer> map = new HashMap<>();

        for (String line : list) {
            String[] words = line.toLowerCase().split("\\s+");

            for (String word : words) {
                Integer count = map.putIfAbsent(word, 1);

                if (count != null) {
                    map.put(word, count + 1);
                }
            }

        }

        return map;
    }
}
