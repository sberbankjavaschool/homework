package ru.sberbank.school.task07;

import lombok.NonNull;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class AnyStringNumberImpl implements AnyStringNumber<List<String>> {

    private FileParser fileParser;

    public AnyStringNumberImpl(FileParser fileParser) {
        this.fileParser = fileParser;
    }

    @Override
    public List<String> findStringsByNumbers(@NonNull String pathToFile,
                                             @NonNull Integer... numbers) throws FileNotFoundException {
        List<String> stringsFile = fileParser.parse(pathToFile);
        List<String> stringsByNumbers = new ArrayList<>();

        for (Integer number : numbers) {
            stringsByNumbers.add(stringsFile.get(number));
        }

        return stringsByNumbers;
    }
}
