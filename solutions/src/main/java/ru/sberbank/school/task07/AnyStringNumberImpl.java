package ru.sberbank.school.task07;

import lombok.NonNull;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class AnyStringNumberImpl implements AnyStringNumber {

    private FileParser parser;

    public AnyStringNumberImpl(@NonNull FileParser fileParser) {
        this.parser = fileParser;
    }

    @Override
    public List<String> findStringsByNumbers(String pathToFile, @NonNull Integer... numbers)
            throws FileNotFoundException {
        List<String> list = parser.parse(pathToFile);
        List<String> result = new ArrayList<>();

        for (Integer n : numbers) {
            if (n != null && n >= 0 && n < list.size()) {
                result.add(list.get(n));
            }
        }

        return result;
    }
}
