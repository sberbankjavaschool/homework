package ru.sberbank.school.task07;

import lombok.NonNull;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

public class ReverseOrderImpl implements ReverseOrder<List<String>> {

    private final FileParser fileParser;

    public ReverseOrderImpl(@NonNull FileParser fileParser) {
        this.fileParser = fileParser;
    }

    @Override
    public List<String> getReverseOrderedStrings(@NonNull String pathToFile) throws FileNotFoundException {
        List<String> resultList = new LinkedList<>();
        List<String> listFromFile = fileParser.parse(pathToFile);
        for (String str : listFromFile) {
            resultList.add(0, str);
        }
        return resultList;
    }
}
