package ru.sberbank.school.task07;

import lombok.NonNull;
import ru.sberbank.school.task07.utill.LineParser;

import java.io.FileNotFoundException;
import java.util.*;

public class DifferentWordsImpl implements DifferentWords {
    private FileParser parser;

    public DifferentWordsImpl(@NonNull FileParser fileParser) {
        this.parser = fileParser;
    }

    @Override
    public Set<String> findSortedDifferentWords(String pathToFile) throws FileNotFoundException {
        List<String> list = parser.parse(pathToFile);
        Set<String> set = new TreeSet<>(getComparator());
        for (String l : list) {
            set.addAll(LineParser.getWords(l));
        }
        return set;
    }

    private static Comparator<String> getComparator() {

        return (o1, o2) -> {
            if (o1.length() == o2.length()) {
                return o1.compareTo(o2);
            }

            return Integer.compare(o1.length(), o2.length());
        };
    }
}
