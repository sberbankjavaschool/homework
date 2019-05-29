package ru.sberbank.school.task07;

import java.io.*;
import java.util.*;

public class DifferentWordsImpl implements DifferentWords<List<String>> {
    private FileParser fileParser;

    public DifferentWordsImpl(FileParser fileParser) {
        this.fileParser = fileParser;
    }

    @Override
    public List<String> findSortedDifferentWords(String pathToFile) throws FileNotFoundException {
        Objects.requireNonNull(pathToFile, "No file name provided");

        List<String> source = fileParser.parse(pathToFile);
        List<String> strings = new ArrayList<>();
        source.forEach(string -> Arrays.asList(string.split("\\P{Alpha}+")).forEach(word -> {
            if (!strings.contains(word)) {
                strings.add(word);
            }
        }));

        strings.sort(new CompareMeAllWayTrough());
        return strings;
    }

    private class CompareMeAllWayTrough implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            if (o1.length() > o2.length()) {
                return 1;
            }
            if (o1.length() < o2.length()) {
                return -1;
            }
            return o1.compareTo(o2);
        }
    }
}
