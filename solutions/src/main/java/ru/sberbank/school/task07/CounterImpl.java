package ru.sberbank.school.task07;

import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by Mart
 * 01.06.2019
 **/
public class CounterImpl implements Counter {
    private FileParser parser;

    public CounterImpl(FileParser parser) {
        Objects.requireNonNull(parser, "FileParser cannot bo null");

        this.parser = parser;
    }

    @Override
    public int count(String pathToFile) throws FileNotFoundException {
        List<String> list = parser.parse(pathToFile);
        Set<String> differentWords = new HashSet<>();

        for (String s : list) {
            String[] words = s.replaceAll("\\p{Punct}", "").trim().split("\\s");
            differentWords.addAll(Arrays.asList(words));
        }
        return differentWords.size();
    }
}
