package ru.sberbank.school.task07;

import java.io.FileNotFoundException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ReverseOrderImpl implements ReverseOrder {

    private final FileParser fileParser;

    public ReverseOrderImpl(FileParser fileParser) {
        this.fileParser = fileParser;
    }

    @Override
    public Object getReverseOrderedStrings(String pathToFile) throws FileNotFoundException {

        List<String> list = fileParser.parse(pathToFile);
        return list.stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }
}
