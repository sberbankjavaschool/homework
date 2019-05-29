package ru.sberbank.school.task07;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnyStringNumberImpl implements AnyStringNumber<Map<Integer, String>> {
    private FileParser fileParser;

    public AnyStringNumberImpl(FileParser fileParser) {
        this.fileParser = fileParser;
    }

    @Override
    public Map<Integer, String> findStringsByNumbers(String pathToFile, Integer... numbers)
            throws FileNotFoundException {
        if (numbers == null || numbers.length == 0) {
            throw new NullPointerException("Numbers should be provided");
        }

        Map<Integer, String> stringMap = new HashMap<>();
        List<String> strings = fileParser.parse(pathToFile);
        for (int number : numbers) {
            stringMap.put(number, strings.get(number));
        }
        return stringMap;
    }
}
