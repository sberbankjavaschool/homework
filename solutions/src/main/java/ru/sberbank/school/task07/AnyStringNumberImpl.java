package ru.sberbank.school.task07;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AnyStringNumberImpl implements AnyStringNumber<List<String>> {
    private FileParser parser;

    public AnyStringNumberImpl(FileParser parser) {
        Objects.requireNonNull(parser, "Парсер не может быть null");
        this.parser = parser;
    }

    @Override
    public List<String> findStringsByNumbers(String pathToFile, Integer... numbers) throws FileNotFoundException {
        Objects.requireNonNull(numbers);

        List<String> strings = parser.parse(pathToFile);
        List<String> result = new ArrayList<>();

        for (Integer num : numbers) {
            if (num != null && num < strings.size()) {
                result.add(strings.get(num));
            }
        }

        return result;
    }
}
