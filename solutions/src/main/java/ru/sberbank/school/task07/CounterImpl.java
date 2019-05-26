package ru.sberbank.school.task07;

import lombok.RequiredArgsConstructor;
import ru.sberbank.school.task06.CountMap;
import ru.sberbank.school.task06.CountMapImpl;

import java.io.FileNotFoundException;
import java.util.List;

/**26.05.2019
 * Реализация на основе {@link CountMapImpl}
 *
 * @author Gregory Melnikov
 */

@RequiredArgsConstructor
public class CounterImpl implements Counter {

    private final FileParser fileParser;

    private final String splitRegex = "[^a-zA-Zа-яА-Я0-9]";

    /**
     * Подсчёт различных слов в файле.
     *
     * @param pathToFile путь до файла
     * @return количество различных слов
     */
    @Override
    public int count(String pathToFile) throws FileNotFoundException {

        List<String> parsedStrings = fileParser.parse(pathToFile);

        CountMap<String> seeker = new CountMapImpl<>();

        for (String string : parsedStrings) {
            String[] stringParts = string.split(splitRegex);
            for (String stringPart : stringParts) {
                seeker.add(stringPart);
            }
        }
        return seeker.size();
    }
}