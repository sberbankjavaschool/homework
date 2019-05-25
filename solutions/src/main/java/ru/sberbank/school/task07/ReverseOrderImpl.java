package ru.sberbank.school.task07;

import lombok.NonNull;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;

public class ReverseOrderImpl implements ReverseOrder<List<String>> {
    private FileParser parser;

    public ReverseOrderImpl(@NonNull FileParser fileParser) {
        this.parser = fileParser;
    }

    @Override
    public List<String> getReverseOrderedStrings(String pathToFile) throws FileNotFoundException {
        List<String> list = parser.parse(pathToFile);
        Collections.reverse(list);
        return list;
    }
}