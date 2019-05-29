package ru.sberbank.school.task07;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class AnyStringNumberImpl implements AnyStringNumber<List<String>> {
    private FileParser fileParser;

    public AnyStringNumberImpl(FileParser fileParser) {
        this.fileParser = fileParser;
    }

    @Override
    public List<String> findStringsByNumbers(String pathToFile, Integer... numbers)
            throws FileNotFoundException {
        if (numbers == null || numbers.length == 0) {
            throw new NullPointerException("Numbers should be provided");
        }

        List<String> stringMap = new ArrayList<>();
        List<String> strings = fileParser.parse(pathToFile);
        for (int number : numbers) {
            stringMap.add(strings.get(number));
        }
        return stringMap;
    }
}
