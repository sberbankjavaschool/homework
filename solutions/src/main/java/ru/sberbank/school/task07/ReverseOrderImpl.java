package ru.sberbank.school.task07;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Задание: Соберите все строки файла в обратном порядке и верните их в подходящей коллекции. В
 * качестве T выберите наиболее подходящую коллекцию.
 */
public class ReverseOrderImpl implements ReverseOrder {
    private FileParser fileParser;

    public ReverseOrderImpl(FileParser fileParser) {
        this.fileParser = fileParser;
    }

    public List<String> getReverseOrderedStrings(String pathToFile) throws FileNotFoundException {
        List<String> reversedList = fileParser.parse(pathToFile);
        Collections.reverse(reversedList);
        return reversedList;
    }
}
