package ru.sberbank.school.task07;

import lombok.NonNull;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class AnyStringNumberImpl implements AnyStringNumber<List<String>> {

    private final FileParser parser;

    public AnyStringNumberImpl(@NonNull FileParser parser) {
        this.parser = parser;
    }

    @Override
    public List<String> findStringsByNumbers(String pathToFile,
                                             @NonNull Integer... numbers) throws FileNotFoundException {
        List<String> fileStrings = parser.parse(pathToFile);
        List<String> result = new ArrayList<>();
        for (Integer index : numbers) {
            if (index < fileStrings.size()) {
                result.add(fileStrings.get(index));
            }
        }
        return result;
    }
}
