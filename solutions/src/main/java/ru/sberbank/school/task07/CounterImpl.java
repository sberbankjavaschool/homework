package ru.sberbank.school.task07;

import lombok.NonNull;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CounterImpl implements Counter {

    private FileParser parser;

    public CounterImpl(@NonNull FileParser parser) {
        this.parser = parser;
    }

    @Override
    public int count(String pathToFile) throws FileNotFoundException {

        List<String> list = parser.parse(pathToFile);
        Set<String> set = new HashSet<>();

        for (String s : list) {
            set.addAll(Arrays.asList(s.toLowerCase().trim().split("\\s+")));
        }

        return set.size();
    }

}
