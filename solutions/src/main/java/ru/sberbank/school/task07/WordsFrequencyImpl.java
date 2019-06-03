package ru.sberbank.school.task07;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Mart
 * 01.06.2019
 **/
public class WordsFrequencyImpl implements WordFrequency<Map<String, Integer>> {
    private FileParser parser;

    public WordsFrequencyImpl(FileParser parser) {
        Objects.requireNonNull(parser, "FileParser cannot bo null");

        this.parser = parser;
    }

    @Override
    public Map<String, Integer> countWords(String pathToFile) throws FileNotFoundException {
        List<String> list = parser.parse(pathToFile);
        Map<String, Integer> map = new HashMap<>();

        for (String s : list) {
            String[] words = s.replaceAll("\\p{Punct}", "").trim().split("\\s");

            for (String word : words) {
                int value = map.getOrDefault(word, 0);
                map.put(word, ++value);
            }
        }
        return map;
    }
}
