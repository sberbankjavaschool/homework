package ru.sberbank.school.task07;

import lombok.NonNull;

import java.io.FileNotFoundException;
import java.util.Set;
import java.util.TreeSet;

public class DifferentWordsImpl implements DifferentWords<Set<String>> {
    private FileParser parser;

    public DifferentWordsImpl(@NonNull FileParser fileParser) {
        this.parser = fileParser;
    }

    @Override
    public Set<String> findSortedDifferentWords(String pathToFile) throws FileNotFoundException {
        Set<String> set = new TreeSet<>((o1, o2) -> o1.length() == o2.length() ?
            o1.compareTo(o2) : o1.length() - o2.length());

        for (String l : parser.parse(pathToFile)) {
            set.addAll(SentenceParser.parse(l));
        }

        return set;
    }
}