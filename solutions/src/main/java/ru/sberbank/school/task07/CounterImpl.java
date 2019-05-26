package ru.sberbank.school.task07;

import java.io.FileNotFoundException;
import java.util.*;
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
        HashSet<String> wordsMap = new HashSet<>();
        for (String word : words) {
            List<String> listwords = Arrays.asList(word.toLowerCase().trim().split("\\s"));
            wordsMap.addAll(listwords);
        }
        return wordsMap.size();
    }
}
