package ru.sberbank.school.task07;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class AnyStringNumberImpl implements AnyStringNumber<List<String>> {
    private FileParser parser;

    public AnyStringNumberImpl(FileParser parser) {
        this.parser = parser;
    }

    @Override
    public List<String> findStringsByNumbers(String pathToFile, Integer... numbers) throws FileNotFoundException {
        List<String> list = parser.parse(pathToFile);

        List<String> result = new ArrayList<>();

        for (int i : numbers) {
            result.add(list.get(i));
        }

        return result;
    }
}
