package ru.sberbank.school.task07;

import lombok.NonNull;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AnyStringNumberImpl implements AnyStringNumber<List<String>> {

    private FileParser parser;

    public AnyStringNumberImpl(@NonNull FileParser parser) {
        this.parser = parser;
    }

    @Override
    public List<String> findStringsByNumbers(String pathToFile, Integer... numbers) throws FileNotFoundException {
        Objects.requireNonNull(numbers, "Список номеров строк не должен быть null");

        List<String> list = parser.parse(pathToFile);
        List<String> result = new ArrayList<>();

        for (Integer i : numbers) {
            if (i != null && i < list.size()) {
                result.add(list.get(i));
            }
        }

        return result;
    }

}
