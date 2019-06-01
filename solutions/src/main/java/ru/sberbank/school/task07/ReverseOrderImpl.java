package ru.sberbank.school.task07;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by Mart
 * 01.06.2019
 **/
public class ReverseOrderImpl implements ReverseOrder<List<String>> {
    private FileParser parser;

    public ReverseOrderImpl(FileParser parser) {
        Objects.requireNonNull(parser, "FileParser cannot bo null");

        this.parser = parser;
    }

    @Override
    public List<String> getReverseOrderedStrings(String pathToFile) throws FileNotFoundException {
        List<String> list = parser.parse(pathToFile);
        Collections.reverse(list);
        return list;
    }
}
