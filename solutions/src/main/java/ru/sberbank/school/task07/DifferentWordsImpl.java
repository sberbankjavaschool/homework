package ru.sberbank.school.task07;

import java.io.FileNotFoundException;
import java.security.PrivateKey;
import java.util.*;

public class DifferentWordsImpl implements DifferentWords<Set<String>> {
    private FileParser parser;

    public DifferentWordsImpl(FileParser parser) {
        Objects.requireNonNull(parser, "Парсер не может быть null");

        this.parser = parser;
    }

    @Override
    public Set<String> findSortedDifferentWords(String pathToFile) throws FileNotFoundException {
        List<String> srcStrings = parser.parse(pathToFile);
        Set<String> resultSet = new TreeSet<>(getComparator());

        for (String string : srcStrings) {
            String[] temp = string.replaceAll("\\p{Punct}", " ")
                    .trim().split("\\s");

            for (String word : temp) {
                if (word.length() > 0) {
                    resultSet.add(word);
                }
            }
        }

        return resultSet;
    }

    private Comparator<String> getComparator() {
        return (s1, s2) -> s1.length() == s2.length() ? s1.compareTo(s2) : s1.length() - s2.length();
    }
}
