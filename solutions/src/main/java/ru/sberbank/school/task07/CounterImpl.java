package ru.sberbank.school.task07;

import lombok.NonNull;

import java.io.FileNotFoundException;
import java.util.*;

public class CounterImpl implements Counter {

    private final FileParser parser;

    public CounterImpl(@NonNull FileParser parser) {
        this.parser = parser;
    }

    @Override
    public int count(String pathToFile) throws FileNotFoundException {
        Set<String> result = new HashSet<>();
        List<String> fileStrings = parser.parse(pathToFile);
        String[] splitted;
        for (String line : fileStrings) {
            splitted = line.toLowerCase().split(" ");
            result.addAll(Arrays.asList(splitted));
        }
        return result.size();
    }
}
