package ru.sberbank.school.task07;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.nio.file.*;
import java.io.IOException;

public class CounterImpl implements Counter {
    /**
     * Подсчёт различных слов в файле.
     *
     * @param pathToFile путь до файла
     * @return количество различных слов
     */
    private FileParser fileParser;

    public CounterImpl(FileParser fileParser) {
        this.fileParser = fileParser;
    }

    public int count(String pathToFile) throws FileNotFoundException {
        List<String> words = fileParser.parse(pathToFile);
        Map<String, Integer> wordsMap = new HashMap<>();
        for (String word : words) {
            wordsMap.put(word, wordsMap.getOrDefault(word, 0) + 1);
        }
        return wordsMap.size();
    }
}
