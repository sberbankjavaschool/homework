package ru.sberbank.school.task07;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ReverseOrderImpl implements ReverseOrder {

    private FileParser parser;

    public ReverseOrderImpl(FileParser parser) {
        this.parser = parser;
    }

    @Override
    public Object getReverseOrderedStrings(String pathToFile) throws FileNotFoundException {

        List<String> list = parser.parse(pathToFile);
        List<String> reverseList = new LinkedList<>();

        for (String str : list) {
            reverseList.add(0, str);
        }

        return reverseList;

    }
}
