package ru.sberbank.school.task07;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

public class CounterImpl implements Counter {
    private FileParser fileParser;

    public CounterImpl(FileParser fileParser) {
        this.fileParser = fileParser;
    }

    @Override
    public int count(String pathToFile) throws FileNotFoundException {
        List<String> strings = fileParser.parse(pathToFile);
        final Integer[] count = {0};
        strings.forEach(string -> Arrays.asList(string.split("\\P{Alpha}+")).forEach(word -> count[0]++));
        return count[0];
    }
}
