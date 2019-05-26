package ru.sberbank.school.task07;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Задание: Верните из метода строки файла, номера которых передаются в метод В качестве T выберите
 * наиболее подходящую коллекцию.
 */
public class AnyStringNumberImpl implements AnyStringNumber {
    private FileParser fileParser;

    public AnyStringNumberImpl(FileParser fileParser) {
        this.fileParser = fileParser;
    }

    public List<String> findStringsByNumbers(String pathToFile, Integer... numbers) throws FileNotFoundException {
        List<String> foundStrings;
        List<String> foundStringsByNumbers = new ArrayList<>();
        foundStrings = fileParser.parse(pathToFile);
        for (Integer i : numbers) {
            if ((i - 1 < foundStrings.size()) && (i - 1 >= 0)) {
                foundStringsByNumbers.add(foundStrings.get(i - 1));
            }
        }
        return foundStringsByNumbers;
    }
}
