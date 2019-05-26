package ru.sberbank.school.task07;

import java.io.FileNotFoundException;
import java.util.*;

public class DifferentWordsImpl implements DifferentWords<Set<String>> {
    private FileParser parser;

    public DifferentWordsImpl(FileParser parser) {
        this.parser = parser;
    }

    @Override
    public Set<String> findSortedDifferentWords(String pathToFile) throws FileNotFoundException {
        List<String> list = parser.parse(pathToFile);

        Set<String> set = new TreeSet<>(Comparator.comparingInt(String::length).thenComparing(o -> o));

        for (String line : list) {
            String[] words = line.toLowerCase().split("\\s+");
            set.addAll(Arrays.asList(words));
        }

        return set;
    }
}
