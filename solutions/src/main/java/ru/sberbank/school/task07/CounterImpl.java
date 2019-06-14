package ru.sberbank.school.task07;

import lombok.NonNull;

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
    public int count(@NonNull String pathToFile) throws FileNotFoundException {
        List<String> stringsFile = fileParser.parse(pathToFile);
        Set<String> countDifWord = new HashSet<>();
        for (String str : stringsFile) {
            countDifWord.addAll(Arrays.asList(str.trim().toLowerCase().split(" ")));
        }
        return countDifWord.size();
    }
}
