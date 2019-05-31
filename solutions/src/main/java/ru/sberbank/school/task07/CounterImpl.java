package ru.sberbank.school.task07;

import lombok.NonNull;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CounterImpl implements Counter {
    private final FileParser fileParser;

    public CounterImpl(@NonNull FileParser fileParser) {
        this.fileParser = fileParser;
    }

    @Override
    public int count(@NonNull String pathToFile) throws FileNotFoundException {
        Set<String> resultSet = new HashSet<>();

        List<String> listFromFile = fileParser.parse(pathToFile);
        for (String s : listFromFile) {
            resultSet.addAll(Arrays.asList(s.split("\\s+")));
        }

        return resultSet.size();
    }
}
