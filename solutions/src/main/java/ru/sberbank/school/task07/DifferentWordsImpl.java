package ru.sberbank.school.task07;

import java.io.FileNotFoundException;
import java.util.*;

public class DifferentWordsImpl implements DifferentWords {

    private FileParser parser;

    public DifferentWordsImpl(FileParser parser) {

        this.parser = parser;
    }

    @Override
    public Object findSortedDifferentWords(String pathToFile) throws FileNotFoundException {

        List<String> list = parser.parse(pathToFile);
        Set<String> listSort = new TreeSet<>(compareList());

        for (String str : list) {
            String[] words = str.toLowerCase().split("\\s+");
            listSort.addAll(Arrays.asList(words));
        }

        return listSort;

    }

    private Comparator<String> compareList() {
        return (o1, o2) -> o1.length() == o2.length() ? o1.compareTo(o2) : o1.length() - o2.length();
    }
}
