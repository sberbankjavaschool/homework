package ru.sberbank.school.task07;

import lombok.NonNull;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class AnyStringNumberImpl implements AnyStringNumber<List<String>> {

    private final FileParser fileParser;

    public AnyStringNumberImpl(@NonNull FileParser fileParser) {
        this.fileParser = fileParser;
    }

    @Override
    public List<String> findStringsByNumbers(@NonNull String pathToFile,
                                             @NonNull Integer... numbers) throws FileNotFoundException {
        List<String> resultList = new ArrayList<>();
        List<String> listFromFile = fileParser.parse(pathToFile);
        for (Integer number : numbers) {
            if (number < 1) {
                throw new IllegalArgumentException("номер сторки должен быть положительным числом");
            } else if (number > listFromFile.size()) {
                throw new IllegalArgumentException("в файле всего " + listFromFile.size() + " строк."
                        + " Строки " + number + " не существует.");
            }
            resultList.add(listFromFile.get(number - 1));
        }
        return resultList;
    }
}
