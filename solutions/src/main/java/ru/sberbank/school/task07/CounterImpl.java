package ru.sberbank.school.task07;

import lombok.NonNull;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

public class CounterImpl implements Counter {

    private FileParser parser;

    public CounterImpl(@NonNull FileParser parser) {
        this.parser = parser;
    }

    @Override
    public int count(String pathToFile) throws FileNotFoundException {
        Set<String> set = new HashSet<>();

        for (String line : parser.parse(pathToFile)) {
            set.addAll(SentenceParser.parse(line));
        }

        return set.size();
    }
}
