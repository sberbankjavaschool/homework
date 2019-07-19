package ru.sberbank.school.task07;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class AnyStringNumberImpl implements AnyStringNumber {

    private final FileParser fileParser;

    public AnyStringNumberImpl(FileParser fileParser) {
        this.fileParser = fileParser;
    }

    @Override
    public List<String> findStringsByNumbers(String pathToFile, Integer... numbers) throws FileNotFoundException {

        List<String> list = fileParser.parse(pathToFile);
        List<String> checkList = new ArrayList<>();

        for (Integer i : numbers) {
            checkList.add(list.get(i));
        }
        return checkList;
    }
}
