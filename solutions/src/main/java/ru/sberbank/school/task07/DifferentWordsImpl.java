package ru.sberbank.school.task07;

import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by Mart
 * 01.06.2019
 **/
public class DifferentWordsImpl implements DifferentWords<Set<String>> {
    private FileParser parser;

    public DifferentWordsImpl(FileParser parser) {
        Objects.requireNonNull(parser, "FileParser cannot bo null");

        this.parser = parser;
    }

    @Override
    public Set<String> findSortedDifferentWords(String pathToFile) throws FileNotFoundException {
        List<String> list = parser.parse(pathToFile);
        Set<String> wordsSet = new TreeSet<>(getLengthThanDefaultComparator());
        for (String s : list) {
            String[] words = s.replaceAll("\\p{Punct}", "").trim().split("\\s");
            wordsSet.addAll(Arrays.asList(words));
        }
        return wordsSet;
    }

    private Comparator<String> getLengthThanDefaultComparator() {
        return (str1, str2) -> str1.length() == str2.length() ? str1.compareTo(str2) : str1.length() - str2.length();
    }
}
