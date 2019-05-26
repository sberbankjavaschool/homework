package ru.sberbank.school.task07;

import lombok.RequiredArgsConstructor;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;

/**
 * 26.05.2019
 * Возвращает результат работы {@link FileParserImpl} с обратным порядком строк
 *
 * @author Gregory Melnikov
 */

@RequiredArgsConstructor
public class ReverseOrderImpl implements ReverseOrder {

    private final FileParser fileParser;

    @Override
    public List<String> getReverseOrderedStrings(String pathToFile) throws FileNotFoundException {

        List<String> parsedStrings = fileParser.parse(pathToFile);

        Collections.reverse(parsedStrings);

        return parsedStrings;
    }
}