package ru.sberbank.school.task07;

import lombok.RequiredArgsConstructor;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * 26.05.2019
 * Создание списка строк с указанными номерами
 *
 * @author Gregory Melnikov
 */

@RequiredArgsConstructor
public class AnyStringNumberImpl implements AnyStringNumber {

    private final FileParser fileParser;

    @Override
    public List<String> findStringsByNumbers(String pathToFile, Integer... numbers) throws FileNotFoundException {

        List<String> result = new ArrayList<>();

        List<String> parsedStrings = fileParser.parse(pathToFile);

        for (Integer number : numbers) {

            if (number < 0 || number >= parsedStrings.size()) {
                throw new IllegalArgumentException("Illegal number: " + number);
            }
            result.add(parsedStrings.get(number));
        }
        return result;
    }
}