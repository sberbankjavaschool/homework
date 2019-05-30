package ru.sberbank.school.task07;

import lombok.NonNull;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class DifferentWordsImpl implements DifferentWords<Set<String>> {

    private final FileParser fileParser;

    public DifferentWordsImpl(@NonNull FileParser fileParser) {
        this.fileParser = fileParser;
    }

    @Override
    public Set<String> findSortedDifferentWords(@NonNull String pathToFile) throws FileNotFoundException {

        Set<String> resultSet = new TreeSet<>((o1, o2) -> {
            if (o1.length() - o2.length() != 0) {
                return o1.length() - o2.length();
            } else {
                return o1.compareTo(o2);
            }
        });

        List<String> listFromFile = fileParser.parse(pathToFile);
        for (String s : listFromFile) {
            resultSet.addAll(Arrays.asList(s.split("\\s+")));
        }

        return resultSet;
    }
}
