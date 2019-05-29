package ru.sberbank.school.task07;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CounterImpl implements Counter {
    private FileParser fileParser;

    public CounterImpl(FileParser fileParser) {
        this.fileParser = fileParser;
    }

    @Override
    public int count(String pathToFile) throws FileNotFoundException {
        List<String> strings = fileParser.parse(pathToFile);
        final Set<String> count = new HashSet<>();
        strings.forEach(string -> count.addAll(Arrays.asList(string.split("\\P{Alpha}+"))));
        return count.size();
    }
}
