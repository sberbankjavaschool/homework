package ru.sberbank.school.task07;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DifferentWordsImpl implements DifferentWords {

    private final FileParser fileParser;

    public DifferentWordsImpl(FileParser fileParser) {
        this.fileParser = fileParser;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<String> findSortedDifferentWords(String pathToFile) throws FileNotFoundException {
        List<String> list = fileParser.parse(pathToFile);
        return (Set<String>) list.stream()
                .map(s -> s.split("[^a-zA-Z]"))
                .flatMap(Arrays::stream)
                .filter(strings -> !strings.equals(""))
                .collect(Collectors.toCollection(getSupplier()));
    }

    private static Supplier getSupplier() {
        Comparator<String> comparator = (o1, o2) -> {
            if (o1.length() == o2.length()) {
                return o1.compareTo(o2);
            }
            return o1.length() - o2.length();
        };

        return () -> new TreeSet<>(comparator);
    }


}
