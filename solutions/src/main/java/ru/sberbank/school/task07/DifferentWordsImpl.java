package ru.sberbank.school.task07;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class DifferentWordsImpl implements DifferentWords {

    private final FileParser fileParser;

    public DifferentWordsImpl(FileParser fileParser) {
        this.fileParser = fileParser;
    }


    @Override
    public Object findSortedDifferentWords(String pathToFile) throws FileNotFoundException {
        List<String> list = fileParser.parse(pathToFile);
        return list.stream()
                .map(s -> s.split("[^a-zA-Z]"))
                .flatMap(Arrays::stream)
                .filter(strings -> !strings.equals(""))
                .sorted(Comparator.comparingInt(String::length))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }



}
