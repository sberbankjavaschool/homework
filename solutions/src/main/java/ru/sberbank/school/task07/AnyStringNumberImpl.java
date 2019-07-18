package ru.sberbank.school.task07;

import lombok.NonNull;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnyStringNumberImpl implements AnyStringNumber<List<String>> {
    private FileParser parser;

    public AnyStringNumberImpl(FileParser parser) {
        this.parser = parser;
    }

    @Override
    public List<String> findStringsByNumbers(String pathToFile,@NonNull Integer... numbers)
        throws FileNotFoundException {

        List<String> list = parser.parse(pathToFile);
        List<String> result = new ArrayList<>();

        Arrays.stream(numbers)
            .filter(i -> i != null && i < list.size() && i > -1)
            .forEach(i -> result.add(list.get(i)));

        return result;
    }
}
