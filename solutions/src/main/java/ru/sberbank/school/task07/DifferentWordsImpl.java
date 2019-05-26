package ru.sberbank.school.task07;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

/**
 * Задание: Соберите список различных слов файла, отсортированный по возрастанию их длины
 * (компаратор сначала по длине слова, потом по тексту). В качестве T выберете наиболее подходящую
 * коллекцию.
 */
public class DifferentWordsImpl implements DifferentWords {
    private FileParser fileParser;

    public DifferentWordsImpl(FileParser fileParser) {
        this.fileParser = fileParser;
    }

    public List<String> findSortedDifferentWords(String pathToFile) throws FileNotFoundException {
        List<String> difWords = fileParser.parse(pathToFile);
        HashSet<String> set = new HashSet<>(difWords);
        difWords.clear();
        difWords.addAll(set);
        difWords.sort(Comparator.comparing(String::length));
        return difWords;
    }
}
