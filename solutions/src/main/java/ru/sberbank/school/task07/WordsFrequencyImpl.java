package ru.sberbank.school.task07;

import java.io.*;
import java.util.*;

public class WordsFrequencyImpl implements WordFrequency<Map<String, Integer>> {
    private FileParser fileParser;

    public WordsFrequencyImpl(FileParser fileParser) {
        this.fileParser = fileParser;
    }

    @Override
    public Map<String, Integer> countWords(String pathToFile) throws FileNotFoundException {
        Objects.requireNonNull(pathToFile, "No file name provided");

        Map<String, Integer> strings = new HashMap<>();

        fileParser.parse(pathToFile).forEach(string -> Arrays.asList(string.split("\\P{Alpha}+"))
                    .forEach(word -> strings.put(word, strings.getOrDefault(word, 0) + 1)));

        return strings;
    }
}
