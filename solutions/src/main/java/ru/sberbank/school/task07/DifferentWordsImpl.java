package ru.sberbank.school.task07;

import lombok.NonNull;

import java.io.FileNotFoundException;
import java.util.*;

public class DifferentWordsImpl implements DifferentWords<Set<String>> {

    private FileParser parser;

    public DifferentWordsImpl(@NonNull FileParser parser) {
        this.parser = parser;
    }

    @Override
    public Set<String> findSortedDifferentWords(String pathToFile) throws FileNotFoundException {

        List<String> list = parser.parse(pathToFile);
        Set<String> set = new TreeSet<>((o1, o2) -> {
            if (o1.length() == o2.length()) {
                return o1.compareToIgnoreCase(o2);
            }

            return o1.length() - o2.length();
        });

        for (String s : list) {
            set.addAll(Arrays.asList(s.toLowerCase().trim().split("\\s+")));
        }

        return set;
    }

}

