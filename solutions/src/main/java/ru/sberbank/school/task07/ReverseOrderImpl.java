package ru.sberbank.school.task07;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ReverseOrderImpl implements ReverseOrder<List<String>> {
    private FileParser parser;

    public ReverseOrderImpl(FileParser parser) {
        Objects.requireNonNull(parser, "Парсер не может быть null");
        this.parser = parser;
    }

    @Override
    public List<String> getReverseOrderedStrings(String pathToFile) throws FileNotFoundException {
        List<String> result = parser.parse(pathToFile);
        Collections.reverse(result);

        return result;
    }
}
