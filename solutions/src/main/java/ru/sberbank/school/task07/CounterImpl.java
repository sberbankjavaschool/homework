package ru.sberbank.school.task07;


import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class CounterImpl implements Counter {

    private final FileParser fileParser;

    public CounterImpl(FileParser fileParser) {
        this.fileParser = fileParser;
    }

    @Override
    public int count(String pathToFile) throws FileNotFoundException {

        List<String> list = fileParser.parse(pathToFile);

        return list.stream()
                .map(s -> s.split("[^a-zA-Z]"))
                .flatMap(Arrays::stream)
                .filter(strings -> !strings.equals(""))
                .collect(Collectors.toSet())
                .size();
    }
}
