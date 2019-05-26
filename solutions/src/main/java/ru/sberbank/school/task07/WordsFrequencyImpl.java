package ru.sberbank.school.task07;

import lombok.RequiredArgsConstructor;
import ru.sberbank.school.task06.CountMap;
import ru.sberbank.school.task06.CountMapImpl;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

/**
 * 26.05.2019
 * По сути копия {@link CounterImpl}, которая возвращает коллекцию
 *
 * @author Gregory Melnikov
 */

@RequiredArgsConstructor
public class WordsFrequencyImpl implements WordFrequency {

    private final FileParser fileParser;

    private final String splitRegex = "[^a-zA-Zа-яА-Я0-9]";

    /**26.05.2019
     * Разбивает входящий файл на слова с помощью fileParser и помещает слова в контейнер {@link CountMapImpl}
     * @param pathToFile путь к файлу
     * @return HashMap, у которой в качестве ключей используются слова из файла,
     *         а в качестве значений - количество повторов в файле.
     */
    @Override
    public Map<String, Integer> countWords(String pathToFile) throws FileNotFoundException {

        List<String> parsedStrings = fileParser.parse(pathToFile);

        CountMap<String> seeker = new CountMapImpl<>();

        for (String string : parsedStrings) {
            String[] stringParts = string.split(splitRegex);
            for (String stringPart : stringParts) {
                seeker.add(stringPart);
            }
        }
        return seeker.toMap();
    }
}