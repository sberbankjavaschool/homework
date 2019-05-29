package ru.sberbank.school.task07;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class AnyStringNumberImpl implements AnyStringNumber {

    private FileParser parser;

    public AnyStringNumberImpl(FileParser parser) {
        this.parser = parser;
    }

    @Override
    public Object findStringsByNumbers(String pathToFile, Integer... numbers) throws FileNotFoundException {

        List<String> list = parser.parse(pathToFile);
        List<String> result = new ArrayList<>();

        for (Integer number : numbers) {
            if (number < list.size() && number != 0) {
                result.add(list.get(number - 1));
            }
        }
        return result;
    }
}
