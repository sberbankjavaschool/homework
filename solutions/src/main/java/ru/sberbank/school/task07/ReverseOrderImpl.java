package ru.sberbank.school.task07;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;

public class ReverseOrderImpl implements ReverseOrder<List<String>> {
    private FileParser parser;

    public ReverseOrderImpl(FileParser parser) {
        this.parser = parser;
    }

    @Override
    public List<String> getReverseOrderedStrings(String pathToFile) throws FileNotFoundException {
        List<String> strings = parser.parse(pathToFile);
        Collections.reverse(strings);
        return strings;
    }
}
