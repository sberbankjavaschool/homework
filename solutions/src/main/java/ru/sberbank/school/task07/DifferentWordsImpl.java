package ru.sberbank.school.task07;

import lombok.NonNull;

import java.io.FileNotFoundException;
import java.util.*;

public class DifferentWordsImpl implements DifferentWords<SortedSet<String>> {

    private final FileParser parser;

    public DifferentWordsImpl(@NonNull FileParser parser) {
        this.parser = parser;
    }

    @Override
    public TreeSet<String> findSortedDifferentWords(String pathToFile) throws FileNotFoundException {
        List<String> fileStrings = parser.parse(pathToFile);
        TreeSet<String> result = new TreeSet<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1.length() > o2.length()) {
                    return 1;
                }
                if (o1.length() == o2.length()) {
                    return o1.compareToIgnoreCase(o2);
                }
                return -1;
            }
        });
        String[] splitted;
        for (String line : fileStrings) {
            splitted = line.toLowerCase().split(" ");
            result.addAll(Arrays.asList(splitted));
        }
        return result;
    }
}
