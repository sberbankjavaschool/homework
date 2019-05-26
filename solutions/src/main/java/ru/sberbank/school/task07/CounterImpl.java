package ru.sberbank.school.task07;

import java.io.FileNotFoundException;
import java.util.*;

public class CounterImpl implements Counter {
    private FileParser parser;

    public CounterImpl(FileParser parser) {
        this.parser = parser;
    }

    @Override
    public int count(String pathToFile) throws FileNotFoundException {
        List<String> list = parser.parse(pathToFile);
        Set<String> set = new HashSet<>();

        for (String line : list) {
            String[] words = line.toLowerCase().split("\\s+");
            set.addAll(Arrays.asList(words));
        }

        return set.size();
    }
}
