package ru.sberbank.school.task07;

import lombok.RequiredArgsConstructor;

import java.io.FileNotFoundException;
import java.util.*;

/**
 * 26.05.2019
 * Формирует список слов из файла, отсортированный по возрастанию их длины
 *
 * @author Gregory Melnikov
 */

@RequiredArgsConstructor
public class DifferentWordsImpl implements DifferentWords {

    private final FileParser fileParser;

    private final String splitRegex = "[^a-zA-Zа-яА-Я0-9]|\\s+";

    @Override
    public Set<String> findSortedDifferentWords(String pathToFile) throws FileNotFoundException {

        List<String> parsedStrings = fileParser.parse(pathToFile);

        Set<String> resultSet = new TreeSet<>(getComparator());

        for (String string : parsedStrings) {
            String[] stringParts = string.split(splitRegex);
            resultSet.addAll(Arrays.asList(stringParts));
        }
        return resultSet;
    }

    private Comparator<String> getComparator() {
        Comparator<String> comparator = new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                if (s1.length() > s2.length()) {
                    return 1;
                } else if (s1.length() < s2.length()) {
                    return -1;
                } else {
                    return s1.compareTo(s2);
                }
            }
        };
        return comparator;
    }
}