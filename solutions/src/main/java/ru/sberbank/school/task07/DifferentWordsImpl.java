package ru.sberbank.school.task07;

import lombok.NonNull;

import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;

public class DifferentWordsImpl implements DifferentWords<Set> {
    private FileParser fileParser;

    public DifferentWordsImpl(FileParser fileParser) {
        this.fileParser = fileParser;
    }

    @Override
    public Set findSortedDifferentWords(@NonNull String pathToFile) throws FileNotFoundException {

        List<String> stringsFile = fileParser.parse(pathToFile);
        Comparator<String> comparator = (o1, o2) -> {
            if (o1.length() > o2.length()) {
                return 1;
            }
            if (o1.length() == o2.length()) {
                return o1.compareToIgnoreCase(o2);
            }
            return -1;
        };

        Set<String> set = new TreeSet<>(comparator);
        for (String str : stringsFile) {
            set.addAll(Arrays.asList(str.trim().toLowerCase().split(" ")));
        }
        return set;
    }
}
