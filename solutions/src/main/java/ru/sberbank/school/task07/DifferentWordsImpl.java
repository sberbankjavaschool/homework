package ru.sberbank.school.task07;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.TreeSet;
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

    public TreeSet<String> findSortedDifferentWords(String pathToFile) throws FileNotFoundException {
        List<String> difWords = fileParser.parse(pathToFile);
        TreeSet<String> set = new TreeSet<>((o1, o2) -> {
            if (o1.length() == o2.length()) {
                return o1.compareToIgnoreCase(o2);
            }
            return o1.length() - o2.length();
        });
        for (String word : difWords) {
            set.addAll(Arrays.asList(word.toLowerCase().trim().split("\\s+")));
        }
        return set;
    }

}
